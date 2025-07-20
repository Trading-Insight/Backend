package com.tradin.core.auth.service.dto;

public record AccessTokenRequestDto(
    String clientId,
    String clientSecret,
    String code,
    String redirectUri,
    String grantType
) {

    public static AccessTokenRequestDto of(String clientId, String clientSecret, String code, String redirectUri) {
        return new AccessTokenRequestDto(clientId, clientSecret, code, redirectUri, "authorization_code");
    }
}
