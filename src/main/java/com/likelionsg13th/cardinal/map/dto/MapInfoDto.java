package com.likelionsg13th.cardinal.map.dto;


import com.likelionsg13th.cardinal.map.domain.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MapInfoDto {
    Long id;
    String position;
    Double latitude;
    Double longitude;

    public static MapInfoDto from (Map map) {
        return new MapInfoDto(
                map.getId(),
                map.getPosition(),
                map.getLatitude(),
                map.getLongitude());
    }

}
