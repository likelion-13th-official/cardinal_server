package com.likelionsg13th.cardinal.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshRequest {
    @NotBlank(message = "refreshToken is required")
    private String refreshToken;
}