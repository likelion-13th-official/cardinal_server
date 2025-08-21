package com.likelionsg13th.cardinal.security.jwt.dto;

public record TokenResponse(String accessToken, String refreshToken, String tokenType) {
    public TokenResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}