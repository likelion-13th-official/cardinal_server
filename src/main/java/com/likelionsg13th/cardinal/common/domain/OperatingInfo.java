package com.likelionsg13th.cardinal.common.domain;

import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Embeddable
@Getter
@Setter
public class OperatingInfo {

    private  LocalTime startTime;
    private LocalTime endTime;

    private boolean isOperating;

}
