package com.likelionsg13th.cardinal.common.dto.resonseDto.map;


import com.likelionsg13th.cardinal.common.domain.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class MapFilteredByCategoryItemDto {

    String label;
    Long labelId;
    MapInfoDto locationInfo;

    public static MapFilteredByCategoryItemDto from(String label, Long labelId, Map map) {

        return new MapFilteredByCategoryItemDto(label, labelId, MapInfoDto.from(map));
    }
}
