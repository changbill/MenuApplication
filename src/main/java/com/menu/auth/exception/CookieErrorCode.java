package com.menu.auth.exception;

import com.menu.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CookieErrorCode implements ErrorCode {
    INVALID_JSON_TYPE(HttpStatus.BAD_REQUEST, "COOKIE_001","Jackson에서 처리할 수 없는 형식입니다."),
    INVALID_DESERIALIZE(HttpStatus.BAD_REQUEST, "COOKIE_002","디코딩할 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
