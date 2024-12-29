package com.tradin.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.exception.ExceptionResponse;
import com.tradin.common.exception.TradinException;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TradinException e) {
            respondException(response, e);
        }
    }

    private void respondException(HttpServletResponse response, TradinException e) throws IOException {
        setResponseHeader(response);
        writeResponse(response, e);
    }

    private void setResponseHeader(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    private void writeResponse(HttpServletResponse response, TradinException e) throws IOException {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .httpStatus(e.getHttpStatus())
                .message(e.getMessage())
                .build();

        response.setStatus(exceptionResponse.getHttpStatus().value());
        response.getWriter().write(toJson(exceptionResponse));
    }

    private String toJson(ExceptionResponse exceptionResponse) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(exceptionResponse);
    }
}
