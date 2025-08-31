package com.likelionsg13th.cardinal.common.provider;

import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.users.dto.resonseDto.ScrapCommonDto;

import java.util.Optional;

/* BOOTH,EVENT,GOODS,PERFORMANCE*/
public interface Scrappable {
    boolean hasCategory(String category);

    /* BOOTH,EVENT,PERFORMANCE*/
    default Optional<ScrapCommonDto> getScrapCommonDto(Long contentId, String day, Boolean isOperating){
        return Optional.empty();
    }

    /*GOODS */
    default Optional<ScrapCommonDto> getScrapCommonDto(Long contentId){
        return Optional.empty();
    }

}
