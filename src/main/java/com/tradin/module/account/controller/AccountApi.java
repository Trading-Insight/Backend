package com.tradin.module.account.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.account.controller.dto.response.AccountsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "계좌", description = "계좌 관련 API")
public interface AccountApi {

    @Operation(summary = "계좌 생성")
    public TradinResponse<String> createAccount(@AuthenticationPrincipal Long userId);

    @Operation(summary = "계좌 목록 조회")
    public TradinResponse<AccountsResponseDto> getAccounts(@AuthenticationPrincipal Long userId);
}
