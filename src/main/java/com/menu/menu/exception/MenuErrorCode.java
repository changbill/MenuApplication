package com.menu.menu.exception;

import com.menu.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum MenuErrorCode implements ErrorCode {
    MENU_NOT_FOUND(BAD_REQUEST, "MENU_001", "메뉴 정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
