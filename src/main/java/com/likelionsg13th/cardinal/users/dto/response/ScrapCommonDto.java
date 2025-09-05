package com.likelionsg13th.cardinal.users.dto.response;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.ContentType.*;


/*
*
* dto : 공통(카테고리,subCategory,썸네일,매장 이름,장소,요일)
- 부스 : + 시작시간,끝시간,
- 이벤트 : + 시작시간,끝시간,
- 공연
    - 아티스트, 동아리 : X
    - 영화제 :  +시작시간,끝시간
- 굿즈 : +가격
*
* category : booth
* subCategory : 포토부스,주점,어쩌고 등등
* */
@Getter @AllArgsConstructor
@Builder
public class ScrapCommonDto {
    Long id;
    String category; // BOOTH,PERFORMANCE,GOODS,EVENT,
    String subCategory; //BOOTH{FOOD_TRUCK,PUB,YARD_PROJECT,PHOTO_BOOTH,PARTNERSHIP} , PERFORMANCE{CLUB,FILM,ARTIST} , NULL
    String thumbnailUrl;
    String name;
    String position;
    List<String> days; //GOODS 일 때 빈 리스트 반환
    ScrapDetail detail; //GOODS -> price , booth,event,film ->  timeDto

    public static ScrapCommonDto of(Booth booth, ScrapDetail detail ){
        return ScrapCommonDto.builder()
                .id(booth.getId())
                .category(BOOTH.toString())
                .subCategory(booth.getCategory().toString())
                .name(booth.getName())
                .thumbnailUrl(booth.getThumbnailUrl())
                .position(booth.getLocation().getPosition())
                .days(booth.getOperatingDays().stream()
                        .map(Enum::toString)
                        .toList())
                .detail(detail)
                .build();
    }

    public static ScrapCommonDto of(Event booth, ScrapDetail detail ){
        return ScrapCommonDto.builder()
                .id(booth.getId())
                .category(EVENT.toString())
                .name(booth.getName())
                .thumbnailUrl(booth.getThumbnailUrl())
                .position(booth.getLocation().getPosition())
                .days(booth.getOperatingDays().stream()
                        .map(Enum::toString)
                        .toList())
                .detail(detail)
                .build();
    }

    public static ScrapCommonDto of(Goods booth, ScrapDetail detail ){
        return ScrapCommonDto.builder()
                .id(booth.getId())
                .category(GOODS.toString())
                .name(booth.getName())
                .thumbnailUrl(booth.getThumbnailUrl())
                .position(booth.getLocation().getPosition())
                .detail(detail)
                .build();
    }

    public static ScrapCommonDto of(Performance booth,ScrapDetail detail){
        return ScrapCommonDto.builder()
                .id(booth.getId())
                .category(PERFORMANCE.toString())
                .subCategory(booth.getCategory().toString())
                .name(booth.getName())
                .thumbnailUrl(booth.getThumbnailUrl())
                .position(booth.getLocation().getPosition())
                .days(booth.getOperatingDays().stream()
                        .map(Enum::toString)
                        .toList())
                .detail(detail)
                .build();
    }
}
