package com.tradin.module.auth.controller;

import com.tradin.common.annotation.DisableAuthInSwagger;
import com.tradin.module.auth.controller.dto.request.TokenReissueRequestDto;
import com.tradin.module.auth.controller.dto.response.TokenResponseDto;
import com.tradin.module.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "구글 로그인&회원가입")
    @DisableAuthInSwagger
    @GetMapping("/google")
    public ResponseEntity<TokenResponseDto> auth(@RequestParam String code) {
        return ResponseEntity.ok(authService.auth(code));
    }

    @Operation(summary = "엑세스 토큰 재발급")
    @DisableAuthInSwagger
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> reissueToken(@Valid @RequestBody TokenReissueRequestDto request) {
        return ResponseEntity.ok(authService.reissueToken(request.toServiceDto()));
    }

    @Operation(summary = "테스트 토큰 발급")
    @DisableAuthInSwagger
    @GetMapping("/test/token")
    public ResponseEntity<TokenResponseDto> issueTestToken() {
        return ResponseEntity.ok(authService.issueTestToken());
    }
}
