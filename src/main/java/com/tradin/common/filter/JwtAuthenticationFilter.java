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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER_PREFIX = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;
    private static final List<String> ALLOW_LIST = List.of("/swagger-ui", "/api-docs", "/health-check", "/v1/auth/cognito", "/v1/auth/token", "/v1/strategies/future", "/v1/strategies/spot", "/v1/histories");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!isAllowList(request.getRequestURI())) {
            String bearerToken = request.getHeader(AUTHORIZATION_HEADER_PREFIX);
            String sub = validateHeaderAndGetSub(bearerToken);
            setAuthentication(sub);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAllowList(String requestURI) {
        return ALLOW_LIST.stream().anyMatch(requestURI::contains);
    }

    private String validateHeaderAndGetSub(String bearerToken) {
        validateHasText(bearerToken);
        validateStartWithBearer(bearerToken);
        return validateAccessTokenAndGetSub(getAccessTokenFromBearer(bearerToken));
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

    private String validateAccessTokenAndGetSub(String accessToken) {
        return jwtUtil.validateToken(accessToken).get("sub", String.class);
    }

    private String getAccessTokenFromBearer(String bearerToken) {
        return bearerToken.substring(BEARER_PREFIX.length());
    }

    private void setAuthentication(String sub) {
        SecurityContextHolder.getContext().setAuthentication(jwtUtil.getAuthentication(sub));
    }
}
