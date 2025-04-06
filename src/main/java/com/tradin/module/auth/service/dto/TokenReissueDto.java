package com.tradin.module.auth.service.dto;

public record TokenReissueDto(String accessToken, String refreshToken) {

    public static TokenReissueDto of(String accessToken, String refreshToken) {
        return new TokenReissueDto(accessToken, refreshToken);
    }
}
