package com.tradin.api.auth;

import com.tradin.api.auth.dto.TokenReissueRequestDto;
import com.tradin.core.common.annotation.DisableAuthInSwagger;
import com.tradin.api.common.response.TradinResponse;

import com.tradin.core.auth.service.AuthFacadeService;
import com.tradin.core.auth.service.dto.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/auth")
public class AuthController implements AuthApi {

    private final AuthFacadeService authFacadeService;

    @DisableAuthInSwagger
    @GetMapping("/google")
    public TradinResponse<TokenResponseDto> auth(@RequestParam String code) {
        return TradinResponse.success(authFacadeService.auth(code));
    }

    @DisableAuthInSwagger
    @PostMapping("/token")
    public TradinResponse<TokenResponseDto> reissueToken(@Valid @RequestBody TokenReissueRequestDto request) {
        return TradinResponse.success(authFacadeService.reissueToken(request.toServiceDto()));
    }
}
