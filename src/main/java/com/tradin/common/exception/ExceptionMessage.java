package com.tradin.common.exception;

import lombok.Getter;

@Getter
public class ExceptionMessage {
    private final String message;

    private final Object data;

    public ExceptionMessage(ExceptionType exceptionType) {
        this.message = exceptionType.getMessage();
        this.data = null;
    }

    public ExceptionMessage(ExceptionType exceptionType, Object data) {
        this.message = exceptionType.getMessage();
        this.data = data;
    }
}