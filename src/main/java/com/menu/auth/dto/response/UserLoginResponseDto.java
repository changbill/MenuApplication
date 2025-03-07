package com.menu.auth.dto.response;

import com.menu.user.domain.User;

public record UserLoginResponseDto(
        Long id,
        String name,
        String email,
        String accessToken,
        String refreshToken
) {

    public static UserLoginResponseDto fromUser(User user, String accessToken, String refreshToken) {
        return new UserLoginResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                accessToken,
                refreshToken
        );
    }
}
