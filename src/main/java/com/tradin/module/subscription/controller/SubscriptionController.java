package com.tradin.module.subscription.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.subscription.controller.dto.FindSubscriptionsResponseDto;
import com.tradin.module.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/accounts/{accountId}")
    public TradinResponse<FindSubscriptionsResponseDto> findSubscriptions(@AuthenticationPrincipal Long userId, Long accountId) {
        return TradinResponse.success(subscriptionService.findSubscriptions(userId, accountId));
    }

}
