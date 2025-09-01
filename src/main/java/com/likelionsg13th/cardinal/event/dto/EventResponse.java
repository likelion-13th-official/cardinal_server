package com.likelionsg13th.cardinal.event.dto;

import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.event.domain.Event;
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
    private boolean isScrapped;

    public EventResponse(Event event, boolean isScrapped) {
        this.id = event.getId();
        this.name = event.getName();
        this.location = event.getLocation().getPosition();
        this.operatingInfo = event.getOperatingInfo();
        this.operatingDays = event.getOperatingDays().stream()
                .map(day -> day.toKorean())
                .collect(Collectors.toList());
        this.thumbnailUrl = event.getThumbnailUrl();
        this.isScrapped = isScrapped;
    }


    public static EventResponse from(Event event, boolean isScrapped) {
        return new EventResponse(event, isScrapped);
    }
}
