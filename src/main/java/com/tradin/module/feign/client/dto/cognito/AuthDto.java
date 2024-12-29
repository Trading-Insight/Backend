package com.tradin.module.feign.client.dto.cognito;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String code;

    public static AuthDto of(String grant_type, String client_id, String redirect_uri, String code) {
        return new AuthDto(grant_type, client_id, redirect_uri, code);
    }
}
