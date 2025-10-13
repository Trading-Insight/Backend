package com.tradin.api.account;

import com.tradin.core.account.service.dto.AccountsResponseDto;
import com.tradin.api.common.response.TradinResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "계좌", description = "계좌 관련 API")
public interface AccountApi {

    @Operation(summary = "계좌 생성")
    public TradinResponse<String> createAccount(@AuthenticationPrincipal Long userId);

    @Operation(summary = "계좌 목록 조회")
    public TradinResponse<AccountsResponseDto> getAccounts(@AuthenticationPrincipal Long userId);

    @Operation(summary = "계좌 잔액 충전 (10000 USDT)")
    public void deposit(@AuthenticationPrincipal Long userId, @PathVariable Long accountId);
}
