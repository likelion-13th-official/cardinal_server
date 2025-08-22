package com.likelionsg13th.cardinal.event.dto;

import com.likelionsg13th.cardinal.common.domain.DetailImage;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.event.domain.Event;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter @Builder
public class EventDetailResponse {
    private Long id;
    private String name;
    private String thumbnailUrl;
    private String location;
    private String description;
    private List<String> detailImageList;

    private OperatingInfo operatingInfo;
    private List<String> operatingDays;

    private boolean bookmarked;

    public static EventDetailResponse from(Event event){
        List<String> imageList = Optional.ofNullable(event.getDetailImageList())
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)          // 리스트 안의 null 요소 방지
                .map(DetailImage::getImageUrl)          // url 뽑기
                .filter(Objects::nonNull)          // url 자체가 null인 경우 방지
                .toList();

        return EventDetailResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .thumbnailUrl(event.getThumbnailUrl())
                .detailImageList(imageList)
                .location(event.getLocation().getPosition())
                .operatingInfo(event.getOperatingInfo())
                .operatingDays(
                        event.getOperatingDays().stream()
                                .map(day -> day.toKorean())
                                .collect(Collectors.toList())
                ).build();

    }
}
