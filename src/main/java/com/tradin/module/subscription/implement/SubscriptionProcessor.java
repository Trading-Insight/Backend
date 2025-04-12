package com.tradin.module.subscription.implement;

import com.tradin.module.account.domain.Account;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.subscription.domain.Subscription;
import com.tradin.module.subscription.domain.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionProcessor {

    private final SubscriptionRepository subscriptionRepository;

    public void activateSubscription(Subscription subscription) {
        subscription.activate();
    }

    public Subscription createSubscription(Account account, Strategy strategy) {
        Subscription subscription = Subscription.builder()
            .account(account)
            .strategy(strategy)
            .build();
        return subscriptionRepository.save(subscription);
    }

    public void deActivateSubscription(Subscription subscription) {
        subscription.deActivate();
    }
}
