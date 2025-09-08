package com.likelionsg13th.cardinal.common.enums;

import java.util.Arrays;
import java.util.List;

public enum DayOfWeek {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일"),
    ALWAYS("상시");

    private final String koreanName;

    DayOfWeek(String koreanName) {
        this.koreanName = koreanName;
    }

    public String toKorean() {
        return koreanName;
    }

    public static DayOfWeek getDayOfWeek(String day) {
       return  Arrays.stream(DayOfWeek.values())
               .filter(dayOfWeek -> day.contains(dayOfWeek.name()))
               .findFirst().orElse(null);
    }
}