package com.tradin.api.subscription;

import com.tradin.api.common.response.TradinResponse;

import com.tradin.core.subscription.service.SubscriptionFacadeService;
import com.tradin.core.subscription.service.dto.FindSubscriptionsResponseDto;
import com.tradin.core.subscription.service.dto.ActivateSubscriptionDto;
import com.tradin.core.subscription.service.dto.DeactivateSubscriptionDto;
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
@RequestMapping("/v1")
public class SubscriptionController implements SubscriptionApi {

    private final SubscriptionFacadeService subscriptionFacadeService;

    @GetMapping("/accounts/{accountId}/subscriptions")
    public TradinResponse<FindSubscriptionsResponseDto> findSubscriptions(@AuthenticationPrincipal Long userId, @PathVariable Long accountId) {
        return TradinResponse.success(subscriptionFacadeService.findSubscriptions(userId, accountId));
    }


    @PostMapping("/accounts/{accountId}/strategies/{strategyId}/subscription")
    public TradinResponse<String> activateAutoTrading(@AuthenticationPrincipal Long userId, @PathVariable Long accountId, @PathVariable Long strategyId) {
        subscriptionFacadeService.activateSubscription(ActivateSubscriptionDto.of(userId, accountId, strategyId));
        return TradinResponse.success();
    }

    @DeleteMapping("/accounts/{accountId}/strategies/{strategyId}/subscription")
    public TradinResponse<String> deactivateAutoTrading(@AuthenticationPrincipal Long userId, @PathVariable Long accountId, @PathVariable Long strategyId) {
        subscriptionFacadeService.deActivateSubscription(DeactivateSubscriptionDto.of(userId, accountId, strategyId));
        return TradinResponse.success();
    }
}
