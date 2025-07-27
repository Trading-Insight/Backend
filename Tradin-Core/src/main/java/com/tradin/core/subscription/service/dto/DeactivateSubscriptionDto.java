package com.tradin.core.subscription.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class DeactivateSubscriptionDto {
    private final Long userId;
    private final Long accountId;
    private final Long strategyId;
} 