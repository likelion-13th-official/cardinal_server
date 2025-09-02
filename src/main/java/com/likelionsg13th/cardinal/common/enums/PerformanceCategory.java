package com.likelionsg13th.cardinal.common.enums;


public enum PerformanceCategory {
    CLUB("동아리"),
    FILM("영화제"),
    ARTIST("아티스트");

    private final String koreanName;

    PerformanceCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public String toKorean() {
        return koreanName;
    }
}
