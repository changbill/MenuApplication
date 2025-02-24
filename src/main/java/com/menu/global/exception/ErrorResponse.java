package com.menu.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String errorCode;
    private String message;

    private ErrorResponse(ErrorCode code) {
        this.status = code.getStatus().value();
        this.errorCode = code.getErrorCode();
        this.message = code.getMessage();
    }

    public static ErrorResponse from(ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code.getStatus().value(), code.getErrorCode(), message);
    }
}
