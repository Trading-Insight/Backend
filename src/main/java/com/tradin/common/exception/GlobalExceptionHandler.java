package com.tradin.common.exception;

import com.tradin.common.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TradinException.class)
    protected ApiResponse<?> handleTradinException(TradinException e) {
        log.error("TradinException: {}", e.getMessage(), e);

        return ApiResponse.error(e.getErrorType(), e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class})
    protected ApiResponse<?> handleBadRequestExceptions(Exception e) {
        log.error("BadRequestException: {}", e.getMessage(), e);

        return ApiResponse.error(ExceptionType.INTERNAL_SERVER_ERROR_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ApiResponse<?> handleAllExceptions(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);

        return ApiResponse.error(ExceptionType.INTERNAL_SERVER_ERROR_EXCEPTION, e.getMessage());
    }
}
