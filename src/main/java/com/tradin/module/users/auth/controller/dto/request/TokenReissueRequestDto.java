package com.tradin.module.users.auth.controller.dto.request;

import com.tradin.module.users.auth.service.dto.TokenReissueDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenReissueRequestDto {

    @NotBlank(message = "Access Token must not be blank")
    private String accessToken;

    @NotBlank(message = "Refresh Token must not be blank")
    private String refreshToken;

    public TokenReissueDto toServiceDto() {
        return TokenReissueDto.of(accessToken, refreshToken);
    }
}
