package com.menu.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> applicationException(BaseException e) {
        ErrorCode code = e.getCode();
        return convertToResponseEntity(code);
    }

    /**
     * 요청 데이터 Validation 전용 ExceptionHandler (@RequestBody)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return convertToResponseEntity(GlobalErrorCode.VALIDATION_ERROR, extractErrorMessage(fieldErrors));
    }

    /**
     * 요청 데이터 Validation 전용 ExceptionHandler (@ModelAttribute)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return convertToResponseEntity(GlobalErrorCode.VALIDATION_ERROR, extractErrorMessage(fieldErrors));
    }

    /**
     * 존재하지 않는 Endpoint 전용 ExceptionHandler
     */
    @ExceptionHandler({NoHandlerFoundException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> noHandlerFoundException() {
        return convertToResponseEntity(GlobalErrorCode.NOT_SUPPORTED_URI_ERROR);
    }

    /**
     * HTTP Request Method 오류 전용 ExceptionHandler
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException() {
        return convertToResponseEntity(GlobalErrorCode.NOT_SUPPORTED_METHOD_ERROR);
    }

    /**
     * MediaType 전용 ExceptionHandler
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpMediaTypeNotSupportedException() {
        return convertToResponseEntity(GlobalErrorCode.NOT_SUPPORTED_MEDIA_TYPE_ERROR);
    }

    /**
     * 내부 서버 오류 전용 ExceptionHandler
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAnyException() {
        return convertToResponseEntity(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }


    private String extractErrorMessage(List<FieldError> fieldErrors) {
        StringBuilder sb = new StringBuilder();
        for(FieldError error : fieldErrors) {
            sb.append(error.getDefaultMessage()).append("\n");
        }
        return sb.toString();
    }

    private static ResponseEntity<ErrorResponse> convertToResponseEntity(ErrorCode code) {
        return ResponseEntity
                .status(code.getStatus())
                .body(ErrorResponse.from(code));
    }

    private static ResponseEntity<ErrorResponse> convertToResponseEntity(ErrorCode code, String message) {
        return ResponseEntity
                .status(code.getStatus())
                .body(ErrorResponse.of(code, message));
    }
}
