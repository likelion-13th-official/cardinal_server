package com.likelionsg13th.cardinal.users.dto.resonseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter @AllArgsConstructor
public final class ScrapTimeDetailDto implements ScrapDetail {
    LocalTime startTime;
    LocalTime endTime;
}
