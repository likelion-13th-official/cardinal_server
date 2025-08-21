package com.likelionsg13th.cardinal.booth.dto;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BoothResponse {

    private Long id;
    private String category;
    private String name;
    private String location;
    private OperatingInfo operatingInfo;
    private List<String> operatingDays;
    private String thumbnailUrl;
    private boolean bookmarked;

    public static BoothResponse from(Booth booth){
        return BoothResponse.builder()
                .id(booth.getId())
                .category(booth.getCategory().toKorean())
                .name(booth.getName())
                .location(booth.getLocation().getPosition())
                .operatingInfo(booth.getOperatingInfo())
                .thumbnailUrl(booth.getThumbnailUrl())
                .operatingDays(
                        booth.getOperatingDays().stream()
                                .map(day -> day.toKorean())
                                .collect(Collectors.toList())
                )
                .build();
    }




}
