package com.likelionsg13th.cardinal.users.dto.response;

import com.likelionsg13th.cardinal.common.enums.ActivityType;
import com.likelionsg13th.cardinal.users.domain.Stamp;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class StampResponse {

    private ActivityType activityType;
    private LocalDateTime earnedAt;
    private String userName;

    public static StampResponse of(Stamp savedStamp) {
        return StampResponse.builder()
                .activityType(savedStamp.getActivityType())
                .earnedAt(savedStamp.getEarnedAt())
                .userName(savedStamp.getUser().getNickname())
                .build();

    }
}
