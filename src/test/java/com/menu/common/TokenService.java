package com.menu.common;

import com.menu.auth.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;

    public static final String BEARER_TOKEN = "Bearer ";

    public String generateAccessToken() {
        return jwtProvider.createAccessToken("test@example.com", "ROLE_USER").getToken();
    }

    public String generateRefreshToken() {
        return jwtProvider.createRefreshToken("test@example.com").getToken();
    }
}
