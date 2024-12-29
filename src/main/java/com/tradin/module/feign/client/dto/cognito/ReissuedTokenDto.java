package com.tradin.module.feign.client.dto.cognito;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ReissuedTokenDto {
    private final String idToken;
    private final String accessToken;
    private final String expiresIn;
    private final String tokenType;
}