package com.tradin.common.filter;

import static com.tradin.common.exception.ExceptionType.EMPTY_HEADER_EXCEPTION;
import static com.tradin.common.exception.ExceptionType.INVALID_BEARER_FORMAT_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.common.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> ALLOW_LIST = List.of("/auth", "/swagger-ui", "/api-docs", "/health-check",
        "/notifications"
    );
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER_PREFIX = "Authorization";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if (!isAllowList(request.getRequestURI())) {
            String bearerToken = request.getHeader(AUTHORIZATION_HEADER_PREFIX);
            Long userId = validateHeaderAndGetUserId(bearerToken);
            setAuthentication(userId);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isAllowList(String requestURI) {
        return ALLOW_LIST.stream().anyMatch(requestURI::contains);
    }


    private Long validateHeaderAndGetUserId(String bearerToken) {
        validateHasText(bearerToken);
        validateStartWithBearer(bearerToken);
        return validateAccessToken(getAccessTokenFromBearer(bearerToken));
    }

    private void validateHasText(String bearerToken) {
        if (!StringUtils.hasText(bearerToken)) {
            throw new TradinException(EMPTY_HEADER_EXCEPTION);
        }
    }

    private void validateStartWithBearer(String bearerToken) {
        if (!bearerToken.startsWith(BEARER_PREFIX)) {
            throw new TradinException(INVALID_BEARER_FORMAT_EXCEPTION);
        }
    }

    private Long validateAccessToken(String accessToken) {
        return jwtUtil.validateAccessToken(accessToken);
    }

    private String getAccessTokenFromBearer(String bearerToken) {
        return bearerToken.substring(BEARER_PREFIX.length());
    }

    private void setAuthentication(Long userId) {
        SecurityContextHolder.getContext().setAuthentication(jwtUtil.getAuthentication(userId));
    }
}
