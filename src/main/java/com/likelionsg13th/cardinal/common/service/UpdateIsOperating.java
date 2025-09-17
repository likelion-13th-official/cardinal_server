package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.repository.BoothRepository;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateIsOperating {
    private final BoothRepository boothRepository;

    /* 실시간 운영 여부 계산*/
    @Scheduled(cron = "0 0,30 0,7-23 * * *", zone = "Asia/Seoul")
    @Transactional
    public void updateOperatingStatus() {
//        List<Booth> booths = boothRepository.findAll();
//
//        /*현재정보*/
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        DayOfWeek currentDayOfWeek = DayOfWeek.from(today.getDayOfWeek());

//        for(Booth booth : booths){
//            //요일 일치하는지 확인
//            boolean operatesToday = booth.getOperatingDays().contains(currentDayOfWeek);
//            if(operatesToday){
//                LocalTime startTime =booth.getOperatingInfo().getStartTime();
//                LocalTime endTime =booth.getOperatingInfo().getEndTime();
//
//                boolean isCurrentlyOperating=!now.isBefore(startTime) && now.isBefore(endTime);
//                booth.getOperatingInfo().setOperating(isCurrentlyOperating);
//            }else{
//                booth.getOperatingInfo().setOperating(false);
//            }
//        }
        // [변경] 1. 운영을 시작해야 할 부스만 조회해서 true로 변경
        List<Booth> boothsToStart = boothRepository
                .findByOperatingInfo_IsOperatingFalseAndOperatingDaysContainingAndOperatingInfo_StartTimeLessThanEqualAndOperatingInfo_EndTimeAfter(
                        currentDayOfWeek, now, now);
        boothsToStart.forEach(booth -> booth.getOperatingInfo().setOperating(true));


        // [변경] 2. 운영을 종료해야 할 부스만 조회해서 false로 변경
        List<Booth> boothsToEnd = boothRepository
                .findByOperatingInfo_IsOperatingTrueAndOperatingDaysNotContainingOrOperatingInfo_IsOperatingTrueAndOperatingInfo_EndTimeLessThanEqual(
                        currentDayOfWeek, now);
        boothsToEnd.forEach(booth -> booth.getOperatingInfo().setOperating(false));


    }
}
