package com.likelionsg13th.cardinal.event.dto;

import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.event.domain.Event;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class EventResponse {
    private long id;

    private String name;

    private String location;
    private OperatingInfo operatingInfo;
    private List<String> operatingDays;

    private String thumbnailUrl;
    private boolean bookmarked;


    public static EventResponse from(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .location(event.getLocation().getPosition())
                .operatingInfo(event.getOperatingInfo())
                .operatingDays(
                        event.getOperatingDays().stream()
                                .map(day->day.toKorean())
                                .collect(Collectors.toList())
                )
                .thumbnailUrl(event.getThumbnailUrl())
                //TODO: 북마크 확인 로직
                .bookmarked(false)
                .build();
    }
}
