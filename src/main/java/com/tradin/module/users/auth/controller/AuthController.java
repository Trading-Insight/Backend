package com.tradin.module.users.auth.controller;

import com.tradin.common.annotation.DisableAuthInSwagger;
import com.tradin.common.response.TradinResponse;
import com.tradin.module.users.auth.controller.dto.request.TokenReissueRequestDto;
import com.tradin.module.users.auth.controller.dto.response.TokenResponseDto;
import com.tradin.module.users.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @DisableAuthInSwagger
    @GetMapping("/google")
    public TradinResponse<TokenResponseDto> auth(@RequestParam String code) {
        return TradinResponse.success(authService.auth(code));
    }

    @Operation(summary = "엑세스 토큰 재발급")
    @DisableAuthInSwagger
    @PostMapping("/token")
    public TradinResponse<TokenResponseDto> reissueToken(@Valid @RequestBody TokenReissueRequestDto request) {
        return TradinResponse.success(authService.reissueToken(request.toServiceDto()));
    }

    @Operation(summary = "테스트 토큰 발급")
    @DisableAuthInSwagger
    @GetMapping("/test/token")
    public TradinResponse<TokenResponseDto> issueTestToken() {
        return TradinResponse.success(authService.issueTestToken());
    }
}
