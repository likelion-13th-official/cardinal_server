package com.likelionsg13th.cardinal.map.dto;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MapListDto {
    String position;
    String category;
    List<MapListItemDto> details;


    public static MapListDto of(List<MapListItemDto> boothList, String category,String position) {
        return MapListDto.builder()
                .category(category)
                .details(boothList)
                .position(position)
                .build();
    }

    public static MapListDto of(List<Booth> boothList, String category,String position,boolean bookMarked) {
        List<MapListItemDto> boothlist =  boothList.stream().map(booth -> MapListItemDto.from(booth,bookMarked)).toList();
        return MapListDto.builder()
                .category(category)
                .position(position)
                .details(boothlist)
                .build();
    }


}
