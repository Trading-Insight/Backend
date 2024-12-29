package com.tradin.module.feign.client.dto.cognito;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ReissueAccessTokenDto {
    private String grant_type;
    private String client_id;
    private String refresh_token;
}
