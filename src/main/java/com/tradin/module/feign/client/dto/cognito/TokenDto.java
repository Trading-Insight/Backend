package com.tradin.module.feign.client.dto.cognito;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenDto {
    private String id_token;
    private String access_token;
    private String refresh_token;
    private String expires_in;
    private String token_type;
}
