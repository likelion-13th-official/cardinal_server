package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
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

    @Scheduled(cron = "0 */30 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void updateOperatingStatus() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        DayOfWeek currentDayOfWeek = DayOfWeek.from(today.getDayOfWeek());
        DayOfWeek yesterdayDayOfWeek = DayOfWeek.from(today.minusDays(1).getDayOfWeek());

        List<Booth> candidates = Stream.concat(
                boothRepository.findAllByOperatingDaysContaining(currentDayOfWeek).stream(),
                Stream.concat(
                        boothRepository.findAllByOperatingDaysContaining(yesterdayDayOfWeek).stream(),
                        boothRepository.findAllByOperatingDaysContaining(DayOfWeek.ALWAYS).stream()
                )
        ).distinct().collect(Collectors.toList());

        for (Booth booth : candidates) {
            boolean shouldBeOperating = isOperatingNow(booth, currentDayOfWeek, yesterdayDayOfWeek, now);
            if (booth.getOperatingInfo().isOperating() != shouldBeOperating) {
                booth.getOperatingInfo().setOperating(shouldBeOperating);
            }
        }
    }


    private boolean isOperatingNow(Booth booth, DayOfWeek today, DayOfWeek yesterday, LocalTime now) {
        List<DayOfWeek> operatingDays = booth.getOperatingDays();

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
        LocalTime startTime = booth.getOperatingInfo().getStartTime();
        LocalTime endTime = booth.getOperatingInfo().getEndTime();
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
