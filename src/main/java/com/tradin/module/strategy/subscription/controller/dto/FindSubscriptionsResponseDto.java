package com.tradin.module.strategy.subscription.controller.dto;

import com.tradin.module.strategy.subscription.domain.repository.dao.SubscriptionsDao;
import java.util.List;

public record FindSubscriptionsResponseDto(List<SubscriptionsDao> subscriptions) {

    public static FindSubscriptionsResponseDto of(List<SubscriptionsDao> subscriptions) {
        return new FindSubscriptionsResponseDto(subscriptions);
    }
}
