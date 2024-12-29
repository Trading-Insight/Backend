package com.tradin.module.feign.client;

import com.tradin.module.feign.client.dto.cognito.JwkDtos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cognitoJwkClient", url = "https://cognito-idp.ap-northeast-2.amazonaws.com/ap-northeast-2_45OUbYhf2/.well-known/jwks.json")
public interface CognitoJwkFeignClient {

    @GetMapping("")
    JwkDtos getJwks();
}
