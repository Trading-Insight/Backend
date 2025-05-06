package com.tradin.common.exception;

import com.tradin.common.response.TradinResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TradinException.class)
    protected ResponseEntity<TradinResponse<?>> handleTradinException(TradinException e) {
        log.error("TradinException: {}", e.getMessage(), e);

        return new ResponseEntity<>(
            TradinResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<TradinResponse<?>> handleAllExceptions(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(
            TradinResponse.error(ExceptionType.INTERNAL_SERVER_ERROR_EXCEPTION, e.getMessage()),
            ExceptionType.INTERNAL_SERVER_ERROR_EXCEPTION.getHttpStatus()
        );
    }
}
