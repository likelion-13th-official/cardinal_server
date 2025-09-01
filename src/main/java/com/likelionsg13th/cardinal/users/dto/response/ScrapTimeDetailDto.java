package com.likelionsg13th.cardinal.users.dto.response;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import com.likelionsg13th.cardinal.event.domain.Event;
import com.likelionsg13th.cardinal.performance.domain.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter @AllArgsConstructor @Builder
public final class ScrapTimeDetailDto implements ScrapDetail {
    LocalTime startTime;
    LocalTime endTime;

    public static ScrapTimeDetailDto from(Booth booth) {
        return ScrapTimeDetailDto.builder()
                .startTime(booth.getOperatingInfo().getStartTime())
                .endTime(booth.getOperatingInfo().getEndTime())
                .build();
    }

    public static ScrapTimeDetailDto from(Event event) {
        return ScrapTimeDetailDto.builder()
                .startTime(event.getOperatingInfo().getStartTime())
                .endTime(event.getOperatingInfo().getEndTime())
                .build();
    }

    public static ScrapTimeDetailDto from(Performance performance) {
        return ScrapTimeDetailDto.builder()
                .startTime(performance.getOperatingInfo().getStartTime())
                .endTime(performance.getOperatingInfo().getEndTime())
                .build();
    }
}
