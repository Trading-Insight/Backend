package com.tradin.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TradinException.class)
    protected ResponseEntity<ExceptionResponse> handleTradinException(TradinException e) {
        log.error("TradinException: {}", e.getMessage(), e);
        ExceptionResponse response = ExceptionResponse.builder()
                .httpStatus(e.getHttpStatus())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class})
    protected ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception e) {
        log.error("BadRequestException: {}", e.getMessage(), e);
        ExceptionResponse response = ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> handleAllExceptions(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        ExceptionResponse response = ExceptionResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Internal Server Error")
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
