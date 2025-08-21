package com.likelionsg13th.cardinal.common.enums;

public enum ContentType {
    BOOTH("부스"),
    EVENT("이벤트"),
    PERFORMANCE("공연"),
    GOODS("굿즈"),
    AMENITY("부대시설");

    private final String koreanName;

    ContentType(String koreanName) {
        this.koreanName = koreanName;
    }

    public String toKorean() {
        return koreanName;
    }
}
