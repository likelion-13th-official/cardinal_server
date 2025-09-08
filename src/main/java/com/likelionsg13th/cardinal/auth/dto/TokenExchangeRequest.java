package com.likelionsg13th.cardinal.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenExchangeRequest(
        @NotBlank(message = "code is required")
        String code
) {}
