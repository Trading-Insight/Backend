package com.tradin.common.fixture;

import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.subscription.domain.Subscription;
import com.tradin.module.users.account.domain.Account;

public class SubscriptionFixture {

    /**
     * 기본 Subscription 생성 (ACTIVE 상태)
     */
    public static Subscription createDefaultSubscription() {
        Strategy strategy = StrategyFixture.createDefaultStrategy();
        Account account = AccountFixture.createDefaultAccount();

        return Subscription.of(
            account,
            strategy
        );
    }

    public static Subscription deActiveSubscription() {
        Subscription subscription = createDefaultSubscription();
        subscription.deActivate();
        return subscription;
    }
}
