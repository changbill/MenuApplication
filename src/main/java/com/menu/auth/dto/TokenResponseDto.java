package com.menu.auth.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
