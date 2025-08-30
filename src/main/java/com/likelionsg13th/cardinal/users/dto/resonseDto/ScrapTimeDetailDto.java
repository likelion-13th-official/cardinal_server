package com.likelionsg13th.cardinal.users.dto.resonseDto;

import com.likelionsg13th.cardinal.booth.domain.Booth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
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
}
