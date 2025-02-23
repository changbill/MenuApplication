package com.menu.store.exception;

import com.menu.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {
    STORE_NOT_FOUND(NOT_FOUND, "STORE_001", "가게 정보를 찾을 수 없습니다."),
    USER_IS_NOT_OWNER(BAD_REQUEST, "STORE_002", "가게 관리자 권한에 접근할 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
