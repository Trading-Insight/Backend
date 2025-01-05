package com.tradin.common.exception;

import com.tradin.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TradinException.class)
    protected ResponseEntity<ApiResponse<?>> handleTradinException(TradinException e) {
        log.error("TradinException: {}", e.getMessage(), e);

        return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<?>> handleAllExceptions(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(ExceptionType.INTERNAL_SERVER_ERROR_EXCEPTION, e.getMessage()), ExceptionType.INTERNAL_SERVER_ERROR_EXCEPTION.getHttpStatus());
    }
}
