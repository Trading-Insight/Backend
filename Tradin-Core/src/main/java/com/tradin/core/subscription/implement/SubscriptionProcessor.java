package com.tradin.core.subscription.implement;


import com.tradin.core.account.domain.Account;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.subscription.domain.Subscription;
import com.tradin.core.subscription.domain.repository.SubscriptionRepository;
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
        return subscriptionRepository.save(subscription);
    }

    public void deActivateSubscription(Subscription subscription) {
        subscription.deActivate();
    }
}
