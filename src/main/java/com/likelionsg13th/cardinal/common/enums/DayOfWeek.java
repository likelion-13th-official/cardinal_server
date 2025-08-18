package com.likelionsg13th.cardinal.common.enums;

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
}