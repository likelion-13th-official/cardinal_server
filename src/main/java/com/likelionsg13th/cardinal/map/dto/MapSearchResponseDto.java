package com.likelionsg13th.cardinal.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MapSearchResponseDto {
    private String position;
    private Double longitude;
    private Double latitude;
    private List<MapSearchItemDto> booths;
}
