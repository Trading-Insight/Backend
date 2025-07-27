package com.tradin.api.users.dto;

import com.tradin.core.users.service.dto.SignUpDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

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
