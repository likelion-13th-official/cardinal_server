package com.likelionsg13th.cardinal.common.domain;

import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Embeddable
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class OperatingInfo {

    private  LocalTime startTime;
    private LocalTime endTime;
   private boolean isOperating;

}

