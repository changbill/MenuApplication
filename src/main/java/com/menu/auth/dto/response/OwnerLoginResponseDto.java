package com.menu.auth.dto.response;

import com.menu.user.domain.User;

public record OwnerLoginResponseDto(
        Long id,
        String name,
        String email,
        String accessToken,
        String refreshToken
) {
    public static OwnerLoginResponseDto fromUser(User user, String accessToken, String refreshToken) {
        return new OwnerLoginResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                accessToken,
                refreshToken
        );
    }
}
