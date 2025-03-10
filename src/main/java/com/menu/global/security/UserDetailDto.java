package com.menu.global.security;

public record UserDetailDto(
        Long userId,
        String email,
        String name,
        String role
) {
}
