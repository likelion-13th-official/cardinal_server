package com.likelionsg13th.cardinal.common.dto.resonseDto.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


/*
* /map?category
  해당 카테고리의 공통 위치와, 카테고리를 반환
* 푸드트럭,주점,공연,굿즈  : map : Map + 카테고리
* */
@Getter
@AllArgsConstructor
public class MapFilteredByCategoryDto {
    String category;
    List<MapInfoDto> locationInfoList;

    public static MapFilteredByCategoryDto from(List<MapInfoDto> locationInfo , String category){
        return new MapFilteredByCategoryDto(
                category,
                locationInfo
        );
    }
}
