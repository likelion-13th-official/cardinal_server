package com.likelionsg13th.cardinal.common.domain;

import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class OperatingInfo {

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private  LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean isOperating;

    //운영여부 계산 메서드 추가

}
