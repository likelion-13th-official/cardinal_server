package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.booth.service.BoothService;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.repository.EventRepository;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import com.likelionsg13th.cardinal.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.likelionsg13th.cardinal.common.enums.DayOfWeek.*;

@Service
@RequiredArgsConstructor
public class UpdateIsOperating {
    private final BoothRepository boothRepository;
    private final EventRepository eventRepository;
    private final PerformanceRepository performanceRepository;
    private final BoothService boothService;

    @Scheduled(cron = "0 */30 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void updateOperatingStatus() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        DayOfWeek currentDayOfWeek = DayOfWeek.from(today.getDayOfWeek());
        DayOfWeek yesterdayDayOfWeek = DayOfWeek.from(today.minusDays(1).getDayOfWeek());

        // booth
        List<Booth> candidates = Stream.concat(
                boothRepository.findAllByOperatingDaysContaining(currentDayOfWeek).stream(),
                Stream.concat(
                        boothRepository.findAllByOperatingDaysContaining(yesterdayDayOfWeek).stream(),
                        boothRepository.findAllByOperatingDaysContaining(DayOfWeek.ALWAYS).stream()
                )
        ).distinct().collect(Collectors.toList());

        for (Booth booth : candidates) {
            boolean shouldBeOperating = BoothisOperatingNow(booth, currentDayOfWeek, yesterdayDayOfWeek, now);
            if (booth.getOperatingInfo().isOperating() != shouldBeOperating) {
                booth.getOperatingInfo().setOperating(shouldBeOperating);
            }
        }

        System.out.println("Booth 스케쥴링 완료");
        boothService.clearAllBoothCaches();


        // event
        List<Event> Ecandidates = Stream.concat(
                eventRepository.findAllByOperatingDaysContaining(currentDayOfWeek).stream(),
                Stream.concat(
                        eventRepository.findAllByOperatingDaysContaining(yesterdayDayOfWeek).stream(),
                        eventRepository.findAllByOperatingDaysContaining(DayOfWeek.ALWAYS).stream()
                )
        ).distinct().collect(Collectors.toList());

        for (Event event : Ecandidates) {
            boolean shouldBeOperating = EventisOperatingNow(event, currentDayOfWeek, yesterdayDayOfWeek, now);
            if (event.getOperatingInfo().isOperating() != shouldBeOperating) {
                event.getOperatingInfo().setOperating(shouldBeOperating);
            }
        }

        System.out.println("Event 스케쥴링 완료");

        // performance

        List<Performance> Pcandidates = Stream.concat(
                performanceRepository.findAllByOperatingDaysContaining(currentDayOfWeek).stream(),
                Stream.concat(
                        performanceRepository.findAllByOperatingDaysContaining(yesterdayDayOfWeek).stream(),
                        performanceRepository.findAllByOperatingDaysContaining(DayOfWeek.ALWAYS).stream()
                )
        ).distinct().collect(Collectors.toList());

        for (Performance perform : Pcandidates) {
            boolean shouldBeOperating = PerformisOperatingNow(perform, currentDayOfWeek, yesterdayDayOfWeek, now);
            if (perform.getOperatingInfo().isOperating() != shouldBeOperating) {
                perform.getOperatingInfo().setOperating(shouldBeOperating);
            }
        }

        System.out.println("Performance 스케쥴링 완료");

    }


    private boolean BoothisOperatingNow(Booth booth, DayOfWeek today, DayOfWeek yesterday, LocalTime now) {
        List<DayOfWeek> operatingDays = booth.getOperatingDays();
        LocalTime startTime = booth.getOperatingInfo().getStartTime();
        LocalTime endTime = booth.getOperatingInfo().getEndTime();


        boolean isWeekdayToday = List.of(MON, TUE, WED, THU, FRI).contains(today);
        // 2. 어제가 평일(월-금)이었는지
        boolean wasWeekdayYesterday = List.of(MON, TUE, WED, THU, FRI).contains(yesterday);

        // 3. 오늘이 실제 운영일인지
        boolean operatesToday = (operatingDays.contains(ALWAYS) && isWeekdayToday) || operatingDays.contains(today);
        // 4. 어제가 실제 운영일이었는지
        boolean operatesYesterday = (operatingDays.contains(ALWAYS) && wasWeekdayYesterday) || operatingDays.contains(yesterday);

        boolean isOvernight = startTime.isAfter(endTime);

        if (!isOvernight) { // 당일 운영
            return operatesToday && !now.isBefore(startTime) && now.isBefore(endTime);
        } else { // 자정 넘어가는 운영
            boolean isContinuingFromYesterday = operatesYesterday && now.isBefore(endTime);
            boolean isStartingToday = operatesToday && !now.isBefore(startTime);
            return isContinuingFromYesterday || isStartingToday;
        }
    }


    private boolean EventisOperatingNow(Event event, DayOfWeek today, DayOfWeek yesterday, LocalTime now) {
        List<DayOfWeek> operatingDays = event.getOperatingDays();

        //'ALWAYS' 처리
        if (operatingDays.contains(ALWAYS)) {
            // "월-금 08시-23시"
            boolean isWeekdayForAlways = List.of(MON, TUE, WED, THU, FRI).contains(today);
            if (!isWeekdayForAlways) {
                return false; // 주말이면 무조건 운영 종료
            }
            LocalTime alwaysStartTime = LocalTime.of(8, 0);
            LocalTime alwaysEndTime = LocalTime.of(23, 0);
            return !now.isBefore(alwaysStartTime) && now.isBefore(alwaysEndTime);
        }

        // 요일이 지정된 일반 부스
        LocalTime startTime = event.getOperatingInfo().getStartTime();
        LocalTime endTime = event.getOperatingInfo().getEndTime();
        boolean isOvernight = startTime.isAfter(endTime);

        if (!isOvernight) { // 당일 운영
            return operatingDays.contains(today) && !now.isBefore(startTime) && now.isBefore(endTime);
        } else { // 자정 넘어가는 운영
            boolean isContinuingFromYesterday = operatingDays.contains(yesterday) && now.isBefore(endTime);
            boolean isStartingToday = operatingDays.contains(today) && !now.isBefore(startTime);
            return isContinuingFromYesterday || isStartingToday;
        }
    }

    private boolean PerformisOperatingNow(Performance perform, DayOfWeek today, DayOfWeek yesterday, LocalTime now) {
        List<DayOfWeek> operatingDays = perform.getOperatingDays();

        //'ALWAYS' 처리
        if (operatingDays.contains(ALWAYS)) {
            // "월-금 08시-23시"
            boolean isWeekdayForAlways = List.of(MON, TUE, WED, THU, FRI).contains(today);
            if (!isWeekdayForAlways) {
                return false; // 주말이면 무조건 운영 종료
            }
            LocalTime alwaysStartTime = LocalTime.of(8, 0);
            LocalTime alwaysEndTime = LocalTime.of(23, 0);
            return !now.isBefore(alwaysStartTime) && now.isBefore(alwaysEndTime);
        }

        // 요일이 지정된 일반 부스
        LocalTime startTime = perform.getOperatingInfo().getStartTime();
        LocalTime endTime = perform.getOperatingInfo().getEndTime();
        boolean isOvernight = startTime.isAfter(endTime);

        if (!isOvernight) { // 당일 운영
            return operatingDays.contains(today) && !now.isBefore(startTime) && now.isBefore(endTime);
        } else { // 자정 넘어가는 운영
            boolean isContinuingFromYesterday = operatingDays.contains(yesterday) && now.isBefore(endTime);
            boolean isStartingToday = operatingDays.contains(today) && !now.isBefore(startTime);
            return isContinuingFromYesterday || isStartingToday;
        }
    }
}
