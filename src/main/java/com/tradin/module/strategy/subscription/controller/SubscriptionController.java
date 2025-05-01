package com.tradin.module.strategy.subscription.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.strategy.subscription.controller.dto.FindSubscriptionsResponseDto;
import com.tradin.module.strategy.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/accounts/{accountId}")
    public TradinResponse<FindSubscriptionsResponseDto> findSubscriptions(@AuthenticationPrincipal Long userId, @PathVariable Long accountId) {
        return TradinResponse.success(subscriptionService.findSubscriptions(userId, accountId));
    }


    @PostMapping("/activate/accounts/{accountId}/strategies/{strategyId}")
    public TradinResponse<String> activateAutoTrading(@AuthenticationPrincipal Long userId, @PathVariable Long accountId, @PathVariable Long strategyId) {
        subscriptionService.activateSubscription(userId, accountId, strategyId);
        return TradinResponse.success();
    }

    @PostMapping("/deactivate/accounts/{accountId}/strategies/{strategyId}")
    public TradinResponse<String> deactivateAutoTrading(@AuthenticationPrincipal Long userId, @PathVariable Long accountId, @PathVariable Long strategyId) {
        subscriptionService.deActivateSubscription(userId, accountId, strategyId);
        return TradinResponse.success();
    }

}
