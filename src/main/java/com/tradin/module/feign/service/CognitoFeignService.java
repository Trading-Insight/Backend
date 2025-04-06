package com.tradin.module.feign.service;

import com.tradin.module.feign.client.dto.cognito.AuthDto;
import com.tradin.module.feign.client.dto.cognito.JwkDtos;
import com.tradin.module.feign.client.dto.cognito.ReissueAccessTokenDto;
import com.tradin.module.feign.client.dto.cognito.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CognitoFeignService {

    @Value("${secret.cognito-client-id}")
    private String cognitoClientId;

    @Value("${secret.cognito-auth-redirect-uri}")
    private String cognitoAuthRedirectUri;

    private final CognitoClient cognitoClient;
    private final CognitoJwkFeignClient cognitoJwkFeignClient;

    public TokenDto getTokenFromCognito(String code) {

        AuthDto authDto = AuthDto.of("authorization_code", cognitoClientId, cognitoAuthRedirectUri, code);

        return cognitoClient.getAccessAndRefreshToken(authDto);
    }

    public String reissueAccessTokenFromCognito(String refreshToken) {
        ReissueAccessTokenDto reissueAccessTokenDto = new ReissueAccessTokenDto("refresh_token",
            cognitoClientId, refreshToken
        );

        return cognitoClient.reissueRefreshToken(reissueAccessTokenDto).getAccessToken();
    }

    public JwkDtos getJwkKey() {
        return cognitoJwkFeignClient.getJwks();
    }
}
