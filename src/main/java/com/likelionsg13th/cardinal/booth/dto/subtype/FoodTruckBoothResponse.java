package com.likelionsg13th.cardinal.booth.dto.subtype;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.subtype.FoodTruckBooth;
import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import com.likelionsg13th.cardinal.common.domain.DetailImage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import com.likelionsg13th.cardinal.booth.domain.Menu;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter @SuperBuilder
public class FoodTruckBoothResponse extends BoothDetailResponse {

    private List<Menu> menus;
    private List<String> menuImageUrls;

    public static FoodTruckBoothResponse of(Booth booth,boolean isScrapped){
        FoodTruckBooth b=(FoodTruckBooth) booth;
        return FoodTruckBoothResponse.builder()
                .id(b.getId())
                .category(b.getCategory().toKorean())
                .name(b.getName())
                .location(b.getLocation().getPosition())
                .description(b.getDescription())
                .operatingInfo(b.getOperatingInfo())
                .thumbnailUrl(b.getThumbnailUrl())
                .operatingDays(

                        b.getOperatingDays().stream()
                                .map(day -> day.toKorean())
                                .collect(Collectors.toList())
                )
                .menus(b.getMenus())
                .menuImageUrls(
                        b.getDetailImageList().stream()
                                .filter(Objects::nonNull) // null인 요소 제거
                                .map(DetailImage::getImageUrl)
                                .collect(Collectors.toList())
                )
//                .menuImageUrls(
//                        Optional.ofNullable(b.getDetailImageList())  // 리스트 자체가 null일 경우
//                                .orElse(Collections.emptyList())
//                                .stream()
//                                .filter(Objects::nonNull)
//                                .map(DetailImage::getImageUrl)
//                                .collect(Collectors.toList())
//                )
                .scrapped(isScrapped)
                .build();

    }

}
