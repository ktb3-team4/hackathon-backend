package com.example.team4backend.dto;

public record AuthTokens(
        TokenResponse accessToken,
        String refreshToken,
        long refreshTokenTtlSeconds
) {
}
