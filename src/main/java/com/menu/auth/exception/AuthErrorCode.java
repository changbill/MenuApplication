package com.menu.auth.exception;

import com.menu.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_001","토큰의 유효기간이 만료되었습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "토큰이 유효하지 않습니다."),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, "AUTH_003", "권한이 없습니다. 로그인 먼저 해주세요."),
    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "AUTH_004", "잘못된 소셜 타입입니다."),
    AUTH_INVALID_REDIRECT_URI(HttpStatus.UNAUTHORIZED, "AUTH_005", "인증되지 않은 redirect URI입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_006", "OAuth2 제공자에서 Email을 받지 못했습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
