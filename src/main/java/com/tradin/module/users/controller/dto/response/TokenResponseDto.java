package com.tradin.module.users.controller.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public static TokenResponseDto of(final String accessToken, final String refreshToken) {
        return new TokenResponseDto(accessToken, refreshToken);
    }
}