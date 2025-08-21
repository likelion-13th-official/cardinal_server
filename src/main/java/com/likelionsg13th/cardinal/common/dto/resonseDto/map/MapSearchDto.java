package com.likelionsg13th.cardinal.common.dto.resonseDto.map;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.enums.ContentType;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static com.likelionsg13th.cardinal.common.enums.ContentType.*;

@Getter@AllArgsConstructor
@Builder
public class MapSearchDto {
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
