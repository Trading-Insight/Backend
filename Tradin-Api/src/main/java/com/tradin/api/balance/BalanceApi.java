package com.tradin.api.balance;

import com.tradin.api.common.response.TradinResponse;
import com.tradin.core.balance.service.dto.BalanceResponseDto;
import com.tradin.core.strategy.domain.CoinType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "잔고", description = "잔고 관련 API")
public interface BalanceApi {

    @Operation(summary = "계좌의 전체 잔고 조회")
    TradinResponse<BalanceResponseDto> findBalancesByAccountId(Long userId, Long accountId);

}
