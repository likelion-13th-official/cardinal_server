package com.likelionsg13th.cardinal.booth.dto.subtype;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.subtype.YardBooth;
import com.likelionsg13th.cardinal.booth.dto.BoothDetailResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.stream.Collectors;

@Getter @SuperBuilder
public class YardBoothResponse extends BoothDetailResponse {

    public static YardBoothResponse of(Booth booth){
        YardBooth b=(YardBooth) booth;
        return YardBoothResponse.builder()
                .id(b.getId())
                .category(b.getCategory().toKorean())
                .name(b.getName())
                .location(b.getLocation().getPosition())
                .operatingInfo(b.getOperatingInfo())
                .thumbnailUrl(b.getThumbnailUrl())
                .description(b.getDescription())
                .operatingDays(
                        b.getOperatingDays().stream()
                                .map(day -> day.toKorean())
                                .collect(Collectors.toList())
                )
                .build();

    }
}
