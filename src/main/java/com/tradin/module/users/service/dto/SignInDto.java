package com.tradin.module.users.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignInDto {
    private String email;
    private String password;

    public static SignInDto of(String email, String password) {
        return new SignInDto(email, password);
    }
}
