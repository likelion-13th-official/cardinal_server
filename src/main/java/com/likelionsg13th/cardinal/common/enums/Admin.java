package com.likelionsg13th.cardinal.common.enums;

public enum Admin {
    PUB_ADMIN("주점 관리자"),
    DEVELOPER("개발자");

    private final String koreanName;

    Admin(String koreanName) {
        this.koreanName = koreanName;
    }

    public String toKorean() {
        return koreanName;
    }
}
