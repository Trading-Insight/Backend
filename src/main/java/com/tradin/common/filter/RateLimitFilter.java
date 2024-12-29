package com.tradin.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.exception.ExceptionResponse;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.tradin.common.exception.ExceptionMessage.IP_RATE_LIMIT_EXCEEDED_EXCEPTION;

@Slf4j
public class RateLimitFilter implements Filter {

    private static final Map<String, Bucket> bucketsByIp = new ConcurrentHashMap<>();
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/swagger-ui", "/api-docs");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
            IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestUri = httpServletRequest.getRequestURI();

        if (!isExcluded(requestUri)) {
            String clientIp = getClientIP(httpServletRequest);
            Bucket requestBucket = bucketsByIp.computeIfAbsent(clientIp, k -> createNewBucketForIp());

            ConsumptionProbe probeForIpBucket = requestBucket.tryConsumeAndReturnRemaining(1);
            if (!probeForIpBucket.isConsumed()) {
                log.warn("최대 요청 횟수 초과 IP: {}, 요청 URI: {}", clientIp, requestUri);
                handleException(httpServletResponse);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isExcluded(String requestUri) {
        return EXCLUDED_PATHS.stream().anyMatch(requestUri::contains);
    }

    private static Bucket createNewBucketForIp() {
        Bandwidth limit = Bandwidth.simple(5, Duration.ofSeconds(1)); // 1초에 3개
        return Bucket.builder().addLimit(limit).build();
    }

    private String getClientIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    private void handleException(HttpServletResponse response) throws IOException {
        response.setStatus(IP_RATE_LIMIT_EXCEEDED_EXCEPTION.getHttpStatus().value());
        response.setContentType("application/json; charset=utf-8");

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .httpStatus(IP_RATE_LIMIT_EXCEEDED_EXCEPTION.getHttpStatus())
                .message(IP_RATE_LIMIT_EXCEEDED_EXCEPTION.getMessage())
                .build();

        response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));
    }
}
