package com.likelionsg13th.cardinal.event.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.likelionsg13th.cardinal.common.domain.OperatingInfo;
import com.likelionsg13th.cardinal.event.domain.Event;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EventSimpleResponse {
    private Long id;
    private String name;
    private OperatingInfo operatingInfo;
    private List<String> operatingDays;

    public static EventSimpleResponse from (Event event){
        return EventSimpleResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .operatingInfo(event.getOperatingInfo())
                //.operatingInfo(event.getOperatingInfo())
                .operatingDays(event.getOperatingDays().stream()
                        .map(day -> day.toKorean())
                        .collect(Collectors.toList()))
                .build();
    }
}
