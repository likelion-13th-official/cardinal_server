package com.likelionsg13th.cardinal.booth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
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

    //JPQL은 생성자만 지원
    public BoothResponse(Booth booth, boolean isScrapped) {
        this.id = booth.getId();
        this.category = booth.getCategory().toKorean();
        this.name = booth.getName();
        this.location = booth.getLocation().getPosition();
        this.operatingInfo = booth.getOperatingInfo();
        this.thumbnailUrl = booth.getThumbnailUrl();
        this.scrapped = isScrapped;
        this.operatingDays = booth.getOperatingDays().stream()
                .map(day -> day.toKorean())
                .collect(Collectors.toList());
    }
    public static BoothResponse from(Booth booth,boolean isScrapped){
        return new BoothResponse(booth,isScrapped);
    }




}
