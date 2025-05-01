package com.tradin.module.users.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleAccessTokenResponseDto(
    @JsonProperty("access_token") String accessToken
) {

}