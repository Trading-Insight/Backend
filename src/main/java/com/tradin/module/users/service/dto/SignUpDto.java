package com.tradin.module.users.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignUpDto {
    private String email;
    private String password;

    public static SignUpDto of(final String accessToken, final String refreshToken) {
        return new SignUpDto(accessToken, refreshToken);
    }
}
