package com.likelionsg13th.cardinal.booth.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.dto.subtype.*;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter @SuperBuilder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class BoothDetailResponse {

    private Long id;
    private String name;
    private String category;

    private OperatingInfo operatingInfo;
    private List<String> operatingDays;

    private String location;
    private String description;

    private String thumbnailUrl;
    @JsonProperty("isScrapped")
    private boolean scrapped;


    public static BoothDetailResponse of(Booth booth, boolean isScrapped) {
        return switch (booth.getCategory()) {
            case PUB -> PubBoothResponse.of(booth,isScrapped);
            case FOOD_TRUCK -> FoodTruckBoothResponse.of(booth,isScrapped);
            case YARD_PROJECT -> YardBoothResponse.of(booth,isScrapped);
            case PHOTO_BOOTH -> PhotoBoothResponse.of(booth,isScrapped);
            case PARTNERSHIP -> PartnershipBoothResponse.of(booth,isScrapped);
            default -> throw new IllegalArgumentException("Invalid booth category");
        };
    }
}
