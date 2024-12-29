package com.tradin.module.feign.client;

import com.tradin.module.feign.client.dto.cognito.AuthDto;
import com.tradin.module.feign.client.dto.cognito.ReissueAccessTokenDto;
import com.tradin.module.feign.client.dto.cognito.ReissuedTokenDto;
import com.tradin.module.feign.client.dto.cognito.TokenDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cognitoClient", url = "https://tradin.auth.ap-northeast-2.amazoncognito.com")
public interface CognitoClient {
    @PostMapping(value = "/oauth2/token", consumes = "application/x-www-form-urlencoded")
    @feign.Headers("Content-Type: application/x-www-form-urlencoded")
    TokenDto getAccessAndRefreshToken(@RequestBody AuthDto authDto);

    @PostMapping(value = "/oauth2/token", consumes = "application/x-www-form-urlencoded")
    ReissuedTokenDto reissueRefreshToken(@RequestBody ReissueAccessTokenDto reissueAccessTokenDto);
}
