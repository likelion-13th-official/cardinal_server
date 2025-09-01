package com.likelionsg13th.cardinal.booth.dto;


import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.booth.dto.subtype.*;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter @SuperBuilder
public class BoothDetailResponse {

    private Long id;
    private String name;
    private String category;

    private OperatingInfo operatingInfo;
    private List<String> operatingDays;

    private String location;
    private String description;

    private String thumbnailUrl;
    private boolean isScrapped;


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
