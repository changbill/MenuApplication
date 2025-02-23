package com.menu.file.exception;

import com.menu.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ErrorCode {
    EMPTY_FILE(BAD_REQUEST, "UPLOAD_001", "전송된 파일이 없습니다."),
    NOT_IMAGE(BAD_REQUEST, "UPLOAD_002", "지원하는 파일이 아닙니다. jpeg 또는 png를 첨부해주세요."),
    S3_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "UPLOAD_003", "파일 업로드에 실패했습니다."),
    INVALID_DIRECTORY(BAD_REQUEST, "UPLOAD_004", "유효하지 않은 경로입니다."),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
