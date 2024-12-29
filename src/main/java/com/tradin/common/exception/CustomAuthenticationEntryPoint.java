package com.tradin.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        HttpStatus status = ExceptionMessage.EMPTY_HEADER_EXCEPTION.getHttpStatus();
        String message = ExceptionMessage.EMPTY_HEADER_EXCEPTION.getMessage();

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .httpStatus(status)
                .message(message)
                .build();

        new ObjectMapper().writeValue(response.getWriter(), exceptionResponse);
    }
}
