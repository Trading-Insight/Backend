package com.tradin.common.exception;

import lombok.Getter;

@Getter
public class TradinException extends RuntimeException {

    private final ExceptionType errorType;

    private final Object data;

    public TradinException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.errorType = exceptionType;
        this.data = null;
    }

    public TradinException(ExceptionType exceptionType, Object data) {
        super(exceptionType.getMessage());
        this.errorType = exceptionType;
        this.data = data;
    }

}