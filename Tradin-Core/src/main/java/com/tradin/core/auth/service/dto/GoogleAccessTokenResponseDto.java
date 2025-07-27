package com.tradin.core.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleAccessTokenResponseDto(
    @JsonProperty("access_token") String accessToken
) {

}