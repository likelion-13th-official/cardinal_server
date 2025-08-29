package com.likelionsg13th.cardinal.users.dto.request;

import com.likelionsg13th.cardinal.common.enums.ActivityType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class StampCreateRequest {
    private ActivityType activityType;
}
