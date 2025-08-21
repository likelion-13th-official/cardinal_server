package com.likelionsg13th.cardinal.common.dto.resonseDto.map;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MapListDto {
    String position;
    String category;
    List<MapListDetailDto> details;


    public static MapListDto from(List<Booth> boothList, String category){

        //booth -> detailDto
        List<MapListDetailDto> details =  boothList.stream().map(
                booth -> {
                    return MapListDetailDto.from(booth,false);
                }).toList();

        String position = boothList.get(0).getLocation().getPosition();

        //detailDto -> listDto
        return new MapListDto(
                position, category, details);
    }
}
