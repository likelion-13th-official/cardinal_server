package com.likelionsg13th.cardinal.booth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.domain.BoothDocument;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class BoothSearchResponse {

    private Long id;
    private String category;
    private String name;
    private String location;
    private String startTime;
    private String endTime;
    private List<String> operatingDays;
    private String thumbnailUrl;
    @JsonProperty("isScrapped")
    private boolean scrapped;

    public static BoothSearchResponse from(BoothDocument doc, boolean isScrapped)
    {
        return BoothSearchResponse.builder()
                .id(doc.getBoothId())
                .name(doc.getName())
                .category(doc.getCategory())
                .location(doc.getLocation())
                .startTime(doc.getStartTime())
                .endTime(doc.getEndTime())
                .operatingDays(doc.getOperatingDays())
                .thumbnailUrl(doc.getThumbnailUrl())
                .scrapped(isScrapped)
                .build();
    }
}
