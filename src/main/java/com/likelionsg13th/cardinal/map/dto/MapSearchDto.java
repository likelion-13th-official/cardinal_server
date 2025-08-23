package com.likelionsg13th.cardinal.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/*
* /map/search?keyword

* */

@Getter@AllArgsConstructor
@Builder
public class MapSearchDto {
    String labelType;
    String label;
    Long labelId;
    String position;
    Double longitude;
    Double latitude;


}
