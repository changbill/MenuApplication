package com.menu.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationExtractor {
    private static final String BEARER_PREFIX = "Bearer";

    public static Optional<String> extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }

        return getToken(authHeader);
    }

    private static Optional<String> getToken(String authHeader) {
        String[] headerParts = authHeader.split(" ");
        if(headerParts.length != 2) {
            return Optional.empty();
        }
        return Optional.ofNullable(headerParts[1]);
    }
}
