package com.tradin.module.users.auth.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.users.auth.controller.dto.request.TokenReissueRequestDto;
import com.tradin.module.users.auth.controller.dto.response.TokenResponseDto;
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

    @Operation(summary = "테스트 토큰 발급")
    TradinResponse<TokenResponseDto> issueTestToken();
}
