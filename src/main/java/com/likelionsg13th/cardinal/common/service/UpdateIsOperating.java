package com.likelionsg13th.cardinal.common.service;

import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UpdateIsOperating {

    /* 운영 여부 계산*/
    public boolean updateOperatingStatus(OperatingInfo operatingInfo, List<DayOfWeek> operatingDays) {
        /*현재 정보*/
        LocalDateTime current = LocalDateTime.now();
        DayOfWeek currentDay = DayOfWeek.getDayOfWeek(current.getDayOfWeek().name());

        boolean isOperatingDay = true;
        boolean isOperatingTime = true;

        // 운영일 또는 운영시간 정보가 없으면 운영 하는것으로 간주
        if (operatingDays != null && !operatingDays.isEmpty()) {
            // 오늘이 운영일인지 확인 (상시 운영 포함)
            isOperatingDay = operatingDays.contains(DayOfWeek.ALWAYS) || operatingDays.contains(currentDay);
        }

        if(operatingInfo != null && operatingInfo.getStartTime() != null && operatingInfo.getEndTime() != null) {
            // 현재 시간이 운영 시간 내에 있는지 확인
            isOperatingTime = current.toLocalTime().isAfter(operatingInfo.getStartTime()) && current.toLocalTime().isBefore(operatingInfo.getEndTime());
        }

        // 운영일과 운영시간이 모두 맞아야 운영 중으로 판단
        return isOperatingDay && isOperatingTime;


    }
}
