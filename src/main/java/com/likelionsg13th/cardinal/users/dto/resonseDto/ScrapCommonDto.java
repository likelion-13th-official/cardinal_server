package com.likelionsg13th.cardinal.users.dto.resonseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


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
@Getter @RequiredArgsConstructor
public class ScrapCommonDto {
    String category; // BOOTH,PERFORMANCE,GOODS,EVENT,
    String subCategory; //BOOTH{FOOD_TRUCK,PUB,YARD_PROJECT,PHOTO_BOOTH,PARTNERSHIP} , PERFORMANCE{CLUB,FILM,ARTIST} , NULL
    String thumbnailUrl;
    String name;
    String position;
    List<String> days; //GOODS 일 때 빈 리스트 반환
    ScrapDetail detail; //GOODS -> price , booth,event,film ->  timeDto

}
