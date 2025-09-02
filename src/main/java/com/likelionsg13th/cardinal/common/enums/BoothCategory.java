package com.likelionsg13th.cardinal.common.enums;

public enum BoothCategory {
  PUB("주점"),
  FOOD_TRUCK("푸드트럭"),
  YARD_PROJECT("마당사업"),
  PHOTO_BOOTH("포토부스"),
  PARTNERSHIP("제휴");

  private final String koreanName;

  BoothCategory(String koreanName) {
    this.koreanName = koreanName;
  }

  public String toKorean() {
    return this.koreanName;
  }
}