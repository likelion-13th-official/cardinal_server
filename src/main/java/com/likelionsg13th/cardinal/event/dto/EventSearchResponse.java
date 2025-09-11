package com.likelionsg13th.cardinal.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.event.domain.EventDocument;
import lombok.Builder;
import lombok.Getter;

import javax.print.Doc;
import java.util.List;

@Getter
@Builder
public class EventSearchResponse {

    private long id;
    private String name;
    private String location;
    private String startTime;
    private String endTime;
    private List<String> operatingDays;
    private String thumbnailUrl;

    @JsonProperty("isScrapped")
    private boolean scrapped;

    public static EventSearchResponse from(EventDocument doc, boolean isScrapped) {
        return EventSearchResponse.builder()
                .id(doc.getEventId())
                .name(doc.getName())
                .location(doc.getLocation())
                .startTime(doc.getStartTime())
                .endTime(doc.getEndTime())
                .operatingDays(doc.getOperatingDays())
                .thumbnailUrl(doc.getThumbnailUrl())
                .scrapped(isScrapped)
                .build();
    }
}
