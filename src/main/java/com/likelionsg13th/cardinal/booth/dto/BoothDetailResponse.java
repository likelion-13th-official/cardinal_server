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
    private boolean bookmarked;


    public static BoothDetailResponse of(Booth booth) {
        return switch (booth.getCategory()) {
            case PUB -> PubBoothResponse.of(booth);
            case FOOD_TRUCK -> FoodTruckBoothResponse.of(booth);
            case YARD_PROJECT -> YardBoothResponse.of(booth);
            case PHOTO_BOOTH -> PhotoBoothResponse.of(booth);
            case PARTNERSHIP -> PartnershipBoothResponse.of(booth);
            default -> throw new IllegalArgumentException("Invalid booth category");
        };
    }
}
