package com.tradin.module.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenReissueDto {
    private final String refreshToken;

    public static TokenReissueDto of(String refreshToken) {
        return new TokenReissueDto(refreshToken);
    }
}
