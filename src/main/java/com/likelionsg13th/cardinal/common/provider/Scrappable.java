package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.users.dto.response.ScrapCommonDto;

import java.util.List;
import java.util.Optional;

import static com.likelionsg13th.cardinal.common.enums.DayOfWeek.ALWAYS;

/* BOOTH,EVENT,GOODS,PERFORMANCE*/
public interface Scrappable  {
    boolean hasCategory(String category);

    /* BOOTH,EVENT,PERFORMANCE*/
     default Optional<ScrapCommonDto> getScrapCommonDto(Long contentId, String day, Boolean isOperating){
         return Optional.empty();
     }

    /*GOODS */
    default Optional<ScrapCommonDto> getScrapCommonDto(Long contentId){
        return Optional.empty();
    }

    /* 요일별 필터링 공통 조건문 : DAY 파라미터 있으며 + (상시도 아니고, 해당 필터링요일과 일치하지 않을 시 FALSE)  */
    default boolean isFilteredByDay(String filterDay, List<DayOfWeek> operatingDays) {

        if(filterDay != null
                && (!operatingDays.contains(DayOfWeek.valueOf(filterDay.toUpperCase()))
                && !operatingDays.contains(ALWAYS)))  return false;
        else return true;
    }

    /* 운영 여부 필터링 공통 조건문 : ISOPERATING 파라미터가 있으며 , 일치 하지 않을 시 FALSE */
     default boolean isFilteredByIsOperating(Boolean filterIsOperating, boolean isOperating) {
         return filterIsOperating == null || isOperating == filterIsOperating;
    }



}
