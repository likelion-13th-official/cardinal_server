package com.likelionsg13th.cardinal.security.jwt.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank String refreshToken) {}
