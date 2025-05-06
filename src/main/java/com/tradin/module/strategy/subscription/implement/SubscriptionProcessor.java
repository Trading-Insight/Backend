package com.tradin.module.strategy.subscription.implement;

import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.subscription.domain.Subscription;
import com.tradin.module.strategy.subscription.domain.repository.SubscriptionRepository;
import com.tradin.module.users.account.domain.Account;
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
        Subscription subscription = Subscription.of(account, strategy);
        account.addSubscription(subscription);
        return subscriptionRepository.save(subscription);
    }

    public void deActivateSubscription(Subscription subscription) {
        subscription.deActivate();
    }
}
