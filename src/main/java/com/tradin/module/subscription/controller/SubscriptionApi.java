package com.tradin.module.subscription.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.subscription.controller.dto.FindSubscriptionsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "구독", description = "전략 구독 관련 API")
public interface SubscriptionApi {

    @Operation(summary = "구독 목록 조회")
    TradinResponse<FindSubscriptionsResponseDto> findSubscriptions(@AuthenticationPrincipal Long userId, @PathVariable Long accountId);

    @Operation(summary = "자동매매 상태 활성화")
    public TradinResponse<String> activateAutoTrading(@AuthenticationPrincipal Long userId, @PathVariable Long accountId, @PathVariable Long strategyId);

    @Operation(summary = "자동매매 상태 비활성화")
    public TradinResponse<String> deactivateAutoTrading(@AuthenticationPrincipal Long userId, @PathVariable Long accountId, @PathVariable Long strategyId);
}
