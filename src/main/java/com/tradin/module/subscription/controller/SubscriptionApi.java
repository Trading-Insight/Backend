package com.tradin.module.subscription.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.subscription.controller.dto.FindSubscriptionsResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "구독", description = "전략 구독 관련 API")
public interface SubscriptionApi {

    @GetMapping("/me/accounts/{accountId}")
    TradinResponse<FindSubscriptionsResponseDto> findSubscriptions(@AuthenticationPrincipal Long userId, Long accountId);
}
