package com.likelionsg13th.cardinal.map.dto;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@Builder
public final class MapSearchTimeDetailDto implements MapSearchDetail {
    private LocalTime startTime;
    private LocalTime endTime;

    public static MapSearchTimeDetailDto from(Booth booth) {
        return MapSearchTimeDetailDto.builder()
                .startTime(booth.getOperatingInfo().getStartTime())
                .endTime(booth.getOperatingInfo().getEndTime())
                .build();
    }

    public static MapSearchTimeDetailDto from(Event event) {
        return MapSearchTimeDetailDto.builder()
                .startTime(event.getOperatingInfo().getStartTime())
                .endTime(event.getOperatingInfo().getEndTime())
                .build();
    }

    public static MapSearchTimeDetailDto from(Performance performance) {
        return MapSearchTimeDetailDto.builder()
                .startTime(performance.getOperatingInfo().getStartTime())
                .endTime(performance.getOperatingInfo().getEndTime())
                .build();
    }
}
