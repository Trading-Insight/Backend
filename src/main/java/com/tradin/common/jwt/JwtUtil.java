package com.tradin.common.jwt;

import static com.tradin.common.exception.ExceptionType.DIFFERENT_REFRESH_TOKEN_EXCEPTION;
import static com.tradin.common.exception.ExceptionType.INVALID_JWT_TOKEN_EXCEPTION;
import static com.tradin.common.exception.ExceptionType.NOT_FOUND_JWT_USERID_EXCEPTION;
import static com.tradin.common.exception.ExceptionType.NOT_FOUND_REFRESH_TOKEN_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.service.UsersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtSecretKeyProvider jwtSecretKeyProvider;
    private final UsersService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    public UUID validateTokensAndGetUserId(String accessToken, String refreshToken) {
        validateTokenClaims(refreshToken);
        return getUserIdFromTokens(accessToken, refreshToken);
    }

    private void validateTokenClaims(String token) {
        parseClaim(token);
    }

    private UUID getUserIdFromTokens(String accessToken, String refreshToken) {
        UUID userId = getUserIdFromAccessToken(accessToken);
        validateExistRefreshToken(refreshToken, userId);
        return userId;
    }

    private void validateExistRefreshToken(String refreshToken, UUID userId) {
        Object refreshTokenFromDb = redisTemplate.opsForValue().get("RT:" + userId);

        if (refreshTokenFromDb == null) {
            throw new TradinException(NOT_FOUND_REFRESH_TOKEN_EXCEPTION);
        }

        if (!refreshToken.equals(refreshTokenFromDb)) {
            throw new TradinException(DIFFERENT_REFRESH_TOKEN_EXCEPTION);
        }
    }

    private Claims parseClaim(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKeyProvider.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (SecurityException e) {
            throw new TradinException(INVALID_JWT_SIGNATURE_EXCEPTION);
        } catch (MalformedJwtException e) {
            throw new TradinException(INVALID_JWT_TOKEN_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new TradinException(EXPIRED_JWT_TOKEN_EXCEPTION);
        } catch (UnsupportedJwtException e) {
            throw new TradinException(UNSUPPORTED_JWT_TOKEN_EXCEPTION);
        } catch (IllegalArgumentException e) {
            throw new TradinException(NOT_FOUND_JWT_CLAIMS_EXCEPTION);
        }
    }

    public UUID getUserIdFromAccessToken(String accessToken) {
        UUID userId = UUID.fromString(parseClaim(accessToken).get("USER_ID", String.class));
        validateExistUserIdFromAccessToken(userId);
        return userId;
    }

    private void validateExistUserIdFromAccessToken(UUID userId) {
        if (userId == null) {
            throw new TradinException(NOT_FOUND_JWT_USERID_EXCEPTION);
        }
    }

    public UUID validateAccessToken(String accessToken) {
        UUID userId = UUID.fromString(parseClaim(accessToken).get("USER_ID", String.class));
        validateExistUserIdFromAccessToken(userId);
        return userId;
    }

    public Authentication getAuthentication(Long userId) {
        Users user = userService.loadUserByUsername(String.valueOf(userId));
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}