package com.likelionsg13th.cardinal.users.dto.response;

import com.likelionsg13th.cardinal.common.enums.ActivityType;
import com.likelionsg13th.cardinal.users.domain.Stamp;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class SimpleStampResponse {
    private ActivityType activityType;


    public static SimpleStampResponse from(Stamp stamp) {
        return SimpleStampResponse.builder()
                .activityType(stamp.getActivityType())
                .build();
    }
}
