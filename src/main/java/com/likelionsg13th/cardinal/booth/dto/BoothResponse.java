package com.likelionsg13th.cardinal.booth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.BoothDocument;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Builder
public class BoothResponse {

    private Long id;
    private String category;
    private String name;
    private String location;
    private OperatingInfo operatingInfo;
    private List<String> operatingDays;
    private String thumbnailUrl;

    @JsonProperty("isScrapped")
    private boolean scrapped;




    public static BoothResponse from(Booth booth, boolean isScrapped){
        return BoothResponse.builder()
                .id(booth.getId())
                .category(booth.getCategory().toKorean())
                .name(booth.getName())
                .location(booth.getLocation().getPosition())
                .operatingInfo(booth.getOperatingInfo())
                .operatingDays(booth.getOperatingDays().stream()
                        .map(day -> day.toKorean())
                        .collect(Collectors.toList()))
                .thumbnailUrl(booth.getThumbnailUrl())
                .scrapped(isScrapped)
                .build();
    }


    public static BoothResponse from(BoothDocument doc, boolean isScrapped) {
        OperatingInfo info=new OperatingInfo(doc.getStartTime(),doc.getEndTime(),true);
        return BoothResponse.builder()
                .id(doc.getBoothId())
                .category(doc.getCategory())
                .name(doc.getName())
                .location(doc.getLocation())
                .operatingInfo(info)
                .operatingDays(doc.getOperatingDays())
                .thumbnailUrl(doc.getThumbnailUrl())
                .scrapped(isScrapped)
                .build();
    }
}
