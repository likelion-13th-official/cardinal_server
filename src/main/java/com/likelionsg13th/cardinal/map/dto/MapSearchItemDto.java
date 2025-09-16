package com.likelionsg13th.cardinal.map.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.domain.Amenity;
import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.goods.domain.Goods;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static com.likelionsg13th.cardinal.common.enums.ContentType.*;
import static com.likelionsg13th.cardinal.common.enums.DayOfWeek.ALWAYS;

@Getter
@AllArgsConstructor
@Builder
public class MapSearchItemDto {
    private Long id;
    private String category;
    private String subCategory;
    private String thumbnailUrl;
    private String name;
    private List<String> days;
    @JsonProperty("isScrapped")
    private boolean scrapped;
    private MapSearchDetail detail;

    public static MapSearchItemDto of(Booth booth, MapSearchDetail detail,boolean isScrapped) {
        return MapSearchItemDto.builder()
                .id(booth.getId())
                .category(BOOTH.toKorean())
                .subCategory(booth.getCategory().toKorean())
                .name(booth.getName())
                .thumbnailUrl(booth.getThumbnailUrl())
                .days(booth.getOperatingDays().stream().map(DayOfWeek::toKorean).toList())
                .scrapped(isScrapped)
                .detail(detail)
                .build();
    }

    public static MapSearchItemDto of(Event event, MapSearchDetail detail,boolean isScrapped) {
        return MapSearchItemDto.builder()
                .id(event.getId())
                .category(EVENT.toKorean())
                .subCategory(null)
                .name(event.getName())
                .thumbnailUrl(event.getThumbnailUrl())
                .days(event.getOperatingDays().stream().map(DayOfWeek::toKorean).toList())
                .scrapped(isScrapped)
                .detail(detail)
                .build();
    }

    public static MapSearchItemDto of(Goods goods, MapSearchDetail detail,boolean isScrapped) {
        return MapSearchItemDto.builder()
                .id(goods.getId())
                .category(GOODS.toKorean())
                .subCategory(null)
                .name(goods.getName())
                .thumbnailUrl(goods.getThumbnailUrl())
                .days(List.of(ALWAYS.toKorean())) // Goods have no operating days
                .scrapped(isScrapped)
                .detail(detail)
                .build();
    }

    public static MapSearchItemDto of(Performance performance, MapSearchDetail detail,boolean isScrapped) {
        System.out.println("days performance debugging ");
        System.out.println(performance.getOperatingDays().stream().map(DayOfWeek::toKorean).toList());
        return MapSearchItemDto.builder()
                .id(performance.getId())
                .category(PERFORMANCE.toKorean())
                .subCategory(performance.getCategory().toKorean())
                .name(performance.getName())
                .thumbnailUrl(performance.getThumbnailUrl())
                .days(performance.getOperatingDays().stream().map(DayOfWeek::toKorean).toList())
                .scrapped(isScrapped)
                .detail(detail)
                .build();
    }

    public static MapSearchItemDto of(Amenity amenity) {
        return MapSearchItemDto.builder()
                .id(amenity.getId())
                .category(AMENITY.toKorean())
                .subCategory(null)
                .name(amenity.getName())
                .thumbnailUrl(null)
                .days(List.of(ALWAYS.toKorean()))
                .scrapped(false)
                .detail(null)
                .build();
    }
}
