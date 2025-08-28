package com.likelionsg13th.cardinal.common.provider;

/*
* 카테고리 별 provider의 상위 provider
* */


import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import com.likelionsg13th.cardinal.common.exception.ScrapNotSupportedForCategory;
import com.likelionsg13th.cardinal.users.dto.UserDto;

public interface CategoryProvider {
    boolean hasCategory(String category);

    //Domain 별 DTO 다름 .Object로 공통 처리
    Object getMapMarkersByCategory();

    /*
    * POST /scraps
    * BOOTH,GOODS,EVENT,PERFORMANCE에만 사용
    * */
    default void addScrapByCategory(Long categoryId, Long userId) {
        throw new ScrapNotSupportedForCategory(ErrorCode.SCRAP_NOT_SUPPORTED_FOR_CATEGORY);
    }
}
