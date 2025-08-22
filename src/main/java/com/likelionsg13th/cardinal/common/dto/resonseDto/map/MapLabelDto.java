package com.likelionsg13th.cardinal.common.dto.resonseDto.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/*
* 검색 결과 , /map?category 일부 카테고리 사용
*
* */

@Getter@AllArgsConstructor
@Builder
public class MapLabelDto {
    String labelType;
    String label;
    Long labelId;
    String position;
    Double longitude;
    Double latitude;

//    public static MapSearchDto from(Booth type){
//        return new MapSearchDto(
//                type.getName(),
//                type.getId(),
//                type.getCategory().name(),
//                type.getLocation().getPosition(),
//                type.getLocation().getLongitude(),
//                type.getLocation().getLatitude()
//        );
//    }
//
//
//    public static MapSearchDto from(Performance type){
//
//        return new MapSearchDto(
//                type.getName(),
//                type.getId(),
//                PERFORMANCE.name(),
//                type.getLocation().getPosition(),
//                type.getLocation().getLongitude(),
//                type.getLocation().getLatitude()
//        );
//    }
//
//    public static MapSearchDto from(Amenity type){
//
//        return new MapSearchDto(
//                type.getName(),
//                type.getId(),
//                AMENITY.name(),
//                type.getLocation().getPosition(),
//                type.getLocation().getLongitude(),
//                type.getLocation().getLatitude()
//        );
//    }
//
//    public static MapSearchDto from(Goods type){
//
//        return new MapSearchDto(
//                type.getName(),
//                type.getId(),
//                GOODS.name(),
//                type.getLocation().getPosition(),
//                type.getLocation().getLongitude(),
//                type.getLocation().getLatitude()
//        );
//    }

}
