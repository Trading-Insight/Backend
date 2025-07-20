package com.tradin.api.auth;

import com.tradin.api.auth.dto.TokenReissueRequestDto;
import com.tradin.core.auth.service.dto.TokenResponseDto;
import com.tradin.api.common.response.TradinResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "인증", description = "인증 관련 API")
public interface AuthApi {

    @Operation(summary = "구글 로그인&회원가입")
    TradinResponse<TokenResponseDto> auth(@RequestParam String code);

    @Operation(summary = "엑세스 토큰 재발급")
    TradinResponse<TokenResponseDto> reissueToken(@Valid @RequestBody TokenReissueRequestDto request);
}
