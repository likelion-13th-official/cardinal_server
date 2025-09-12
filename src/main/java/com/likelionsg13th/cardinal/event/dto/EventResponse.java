package com.likelionsg13th.cardinal.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.event.domain.EventDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder @AllArgsConstructor
public class EventResponse {
    private long id;

    private String name;

    private String location;
    private OperatingInfo operatingInfo;
    private List<String> operatingDays;

    private String thumbnailUrl;
    @JsonProperty("isScrapped")
    private boolean scrapped;

    public static EventResponse from(Event event, boolean isScrapped) {

        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .location(event.getLocation().getPosition())
                .operatingDays(event.getOperatingDays().stream()
                        .map(day -> day.toKorean())
                        .collect(Collectors.toList()))
                .operatingInfo(event.getOperatingInfo())
                .thumbnailUrl(event.getThumbnailUrl())
                .scrapped(isScrapped)
                .build();

    }

    public static EventResponse from(EventDocument doc, boolean isScrapped) {
        OperatingInfo info=new OperatingInfo(doc.getStartTime(),doc.getEndTime(),true);
        return EventResponse.builder()
                .id(doc.getEventId())
                .name(doc.getName())
                .location(doc.getLocation())
                .operatingInfo(info)
                .operatingDays(doc.getOperatingDays())
                .thumbnailUrl(doc.getThumbnailUrl())
                .scrapped(isScrapped)
                .build();
    }
}
