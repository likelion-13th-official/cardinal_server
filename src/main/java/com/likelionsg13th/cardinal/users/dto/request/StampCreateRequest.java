package com.likelionsg13th.cardinal.users.dto.request;

import com.likelionsg13th.cardinal.common.enums.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class StampCreateRequest {
    @NotNull
    private ActivityType activityType;
}
