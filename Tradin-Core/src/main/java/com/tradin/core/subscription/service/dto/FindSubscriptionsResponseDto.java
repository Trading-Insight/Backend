package com.tradin.core.subscription.service.dto;

import com.tradin.core.subscription.domain.repository.dao.SubscriptionsDao;
import java.util.List;

public record FindSubscriptionsResponseDto(List<SubscriptionsDao> subscriptions) {

    public static FindSubscriptionsResponseDto of(List<SubscriptionsDao> subscriptions) {
        return new FindSubscriptionsResponseDto(subscriptions);
    }
}
