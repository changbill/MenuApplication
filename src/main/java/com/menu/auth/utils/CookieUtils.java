package com.menu.auth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.menu.auth.domain.CookieName;
import com.menu.auth.exception.CookieErrorCode;
import com.menu.global.exception.BaseException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
public class CookieUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<Cookie> getCookie(HttpServletRequest request, CookieName cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName.getValue())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, CookieName cookieName, String value, int maxAge) {
        Cookie cookie = new Cookie(cookieName.getValue(), value);
        cookie.setPath("/");        // 모든 경로에서 사용가능
        cookie.setHttpOnly(true);   // JavaScript에서 쿠키를 접근할 수 없도록 보호(XSS 공격 방지)
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, CookieName cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName.getValue())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(object);
            return Base64.getUrlEncoder().encodeToString(bytes);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw BaseException.type(CookieErrorCode.INVALID_JSON_TYPE);
        }
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(cookie.getValue());
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw BaseException.type(CookieErrorCode.INVALID_DESERIALIZE);
        }
    }
}
