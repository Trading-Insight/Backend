package com.tradin.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ExceptionResponse {
    private final HttpStatus httpStatus;
    private final String message;
}