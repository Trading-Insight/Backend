package com.tradin.api.users.dto;

import com.tradin.core.users.service.dto.SignInDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

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
