package com.likelionsg13th.cardinal.pub.dto.resonseDto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PubDetailResponseDto {
    //섬네일, 북마크, 인스타 주소, 요일, 시작시간, 종료 시간,
    //대ㅠ표 메뉴, 공지사항, 부스 소개, 메뉴판 사진
    Long pub_id;
    String pub_name ;
    String department;
    String notice;
    String description;
    String dayOfWeek;
    String thumbnailUrl;
    String menuImageUrl;
    String instaUrl;
    LocalTime startTime;
    LocalTime endTime;
    List<String> menuList = new ArrayList<>();
    boolean isMarked;
}
