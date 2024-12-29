package com.tradin.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TradinException extends RuntimeException {

    private final HttpStatus httpStatus;

    public TradinException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.httpStatus = exceptionMessage.getHttpStatus();
    }
}