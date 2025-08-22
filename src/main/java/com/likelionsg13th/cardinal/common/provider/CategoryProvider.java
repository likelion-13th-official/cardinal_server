package com.likelionsg13th.cardinal.common.provider;

/*
* 카테고리 별 provider의 상위 provider
* */


public interface CategoryProvider {
    boolean hasCategory(String category);

    //Domain 별 DTO 다름 .Object로 공통 처리
    Object getMapMarkersByCategory();

}
