package com.likelionsg13th.cardinal.common.domain;

import com.likelionsg13th.cardinal.common.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class OperationAwareEntity {

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> operatingDays;

    @Embedded
    private OperatingInfo operatingInfo;

    private boolean isOperating;


}

