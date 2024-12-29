package com.tradin.module.users.controller.dto.request;

import com.tradin.module.users.service.dto.SignUpDto;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;

    public SignUpDto toServiceDto() {
        return SignUpDto.of(email, password);
    }
}
