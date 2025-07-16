package com.tradin.module.strategy.subscription.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.strategy.subscription.controller.dto.FindSubscriptionsResponseDto;
import com.tradin.module.strategy.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/accounts/{accountId}/subscriptions")
    public TradinResponse<FindSubscriptionsResponseDto> findSubscriptions(@AuthenticationPrincipal Long userId, @PathVariable Long accountId) {
        return TradinResponse.success(subscriptionService.findSubscriptions(userId, accountId));
    }


    @PostMapping("/accounts/{accountId}/strategies/{strategyId}")
    public TradinResponse<String> activateAutoTrading(@AuthenticationPrincipal Long userId, @PathVariable Long accountId, @PathVariable Long strategyId) {
        subscriptionService.activateSubscription(userId, accountId, strategyId);
        return TradinResponse.success();
    }

    @DeleteMapping("/accounts/{accountId}/strategies/{strategyId}")
    public TradinResponse<String> deactivateAutoTrading(@AuthenticationPrincipal Long userId, @PathVariable Long accountId, @PathVariable Long strategyId) {
        subscriptionService.deActivateSubscription(userId, accountId, strategyId);
        return TradinResponse.success();
    }

    @Operation(summary = "테스트 전략 전체 구독")
    @PostMapping("/accounts/strategies/{strategyId}")
    public TradinResponse<String> activateAutoTradingTest(@AuthenticationPrincipal Long userId, @PathVariable Long strategyId) {
        subscriptionService.activateSubscriptionTest(userId, strategyId);
        return TradinResponse.success();
    }

    @Operation(summary = "테스트 전략 전체 구독 해제")
    @PostMapping("/accounts/strategies/{strategyId}")
    public TradinResponse<String> deactivateAutoTradingTest(@AuthenticationPrincipal Long userId, @PathVariable Long strategyId) {
        subscriptionService.deActivateSubscriptionTest(userId, strategyId);
        return TradinResponse.success();
    }
}
