package com.likelionsg13th.cardinal.pub.dto.resonseDto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PubsResponseDto {
    //단과, 주점명, 대표 메뉴리스트 ,북마크 여부
    Long pub_id;
    String pub_name ;
    String department;
    List<String> menuList = new ArrayList<>();
    boolean isMarked;
}
