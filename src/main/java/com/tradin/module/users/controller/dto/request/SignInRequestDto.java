package com.tradin.module.users.controller.dto.request;

import com.tradin.module.users.service.dto.SignInDto;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
public class SignInRequestDto {
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;

    public SignInDto toServiceDto() {
        return SignInDto.of(email, password);
    }
}
