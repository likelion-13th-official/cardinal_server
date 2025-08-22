package com.likelionsg13th.cardinal.common.dto.resonseDto.map;

import com.likelionsg13th.cardinal.common.domain.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MapFilteredByCategoryDetailDto {

    String  category;
    List<MapFilteredByCategoryItemDto> items;
    //카테고리 , 리스트 ( 위치 정보, 가게 정보 )

    public static MapFilteredByCategoryDetailDto of(String category,List<MapFilteredByCategoryItemDto> items){
        return new MapFilteredByCategoryDetailDto(category,items);
    }

}
