package com.tradin.common.jwt;

import com.tradin.module.auth.controller.dto.response.TokenResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtSecretKeyProvider jwtSecretKeyProvider;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;     // 7일
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일

    public TokenResponseDto createJwtToken(Long userId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        // Access Token 생성
        String accessToken = Jwts.builder()
            .claim("USER_ID", userId)
            .setExpiration(accessTokenExpiresIn)
            .signWith(jwtSecretKeyProvider.getSecretKey(), SignatureAlgorithm.HS512)
            .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
            .setExpiration(refreshTokenExpiresIn)
            .signWith(jwtSecretKeyProvider.getSecretKey(), SignatureAlgorithm.HS512)
            .compact();

        redisTemplate.opsForValue().set("RT:" + userId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return TokenResponseDto.of(accessToken, refreshToken);
    }
}