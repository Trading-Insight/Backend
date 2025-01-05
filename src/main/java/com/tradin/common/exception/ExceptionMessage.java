package com.tradin.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionMessage {
    private final HttpStatus status;
    private final String message;
    private final Object data;

    public ExceptionMessage(ExceptionType exceptionType) {
        this.status = exceptionType.getHttpStatus();
        this.message = exceptionType.getMessage();
        this.data = null;
    }

    public ExceptionMessage(ExceptionType exceptionType, Object data) {
        this.status = exceptionType.getHttpStatus();
        this.message = exceptionType.getMessage();
        this.data = data;
    }
}