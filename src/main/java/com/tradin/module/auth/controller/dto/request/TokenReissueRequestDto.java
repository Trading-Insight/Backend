package com.tradin.module.auth.controller.dto.request;

import com.tradin.module.auth.service.dto.TokenReissueDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenReissueRequestDto {
    @NotBlank(message = "Refresh Token must not be blank")
    private String refreshToken;

    public TokenReissueDto toServiceDto() {
        return TokenReissueDto.of(refreshToken);
    }
}
