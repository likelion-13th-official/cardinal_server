package com.likelionsg13th.cardinal.users.dto.request;

import com.likelionsg13th.cardinal.common.enums.ActivityType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class StampCreateRequest {
    @NotBlank(message="활동 타입을 입력해주세요.")
    private ActivityType activityType;
}
