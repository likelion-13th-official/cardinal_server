package com.likelionsg13th.cardinal.common.dto.resonseDto.map;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter@AllArgsConstructor
public class MapSearchDto {

    String label;
    String labelId;
    String labelType;
    String position;
    String longitude;
    String latitude;


}
