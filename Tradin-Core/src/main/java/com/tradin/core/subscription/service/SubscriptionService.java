package com.tradin.core.subscription.service;

import static com.tradin.core.common.exception.ExceptionType.ALREADY_POSITION_EXIST_EXCEPTION;
import static com.tradin.core.common.exception.ExceptionType.ALREADY_SUBSCRIBED_EXCEPTION;

import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.account.domain.Account;
import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.subscription.domain.Subscription;
import com.tradin.core.subscription.domain.repository.SubscriptionRepository;
import com.tradin.core.subscription.service.dto.FindSubscriptionsResponseDto;
import com.tradin.core.subscription.service.dto.ActivateSubscriptionDto;
import com.tradin.core.subscription.service.dto.DeactivateSubscriptionDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public FindSubscriptionsResponseDto findSubscriptions(Long accountId) {
        return FindSubscriptionsResponseDto.of(subscriptionRepository.findAllByUserIdAndAccountId(accountId));
    }

    public void activateSubscription(Account account, Strategy strategy) {
        validateExistActiveSubscriptionByAccountIdAndCoinType(account.getId(), strategy.getCoinType());
        activateSubscriptionEntity(account, strategy);
    }

    public void deActivateSubscription(Account account, Strategy strategy) {
        deActivateSubscriptionEntity(account, strategy);
    }

    public void activateSubscriptionTest(List<Account> accounts, Strategy strategy) {
        for (Account account : accounts) {
            activateSubscription(account, strategy);
        }
    }

    public void deActivateSubscriptionTest(List<Account> accounts, Strategy strategy) {
        for (Account account : accounts) {
            deActivateSubscription(account, strategy);
        }
    }

    public List<Account> findSubscribedAccountsByStrategyId(Long strategyId) {
        return subscriptionRepository.findSubscribedAccountsByStrategyId(strategyId);
    }

    private void validateExistActiveSubscriptionByAccountIdAndCoinType(Long accountId, CoinType coinType) {
        subscriptionRepository.findSubscriptionByAccountIdAndCoinType(accountId, coinType).ifPresent(subscription -> {
            if (!subscription.isDeActivated()) {
                throw new TradinException(ALREADY_SUBSCRIBED_EXCEPTION);
            }
        });
    }

    private void deActivateSubscriptionEntity(Account account, Strategy strategy) {
        Subscription subscription = subscriptionRepository.findByAccountIdAndStrategyId(account.getId(), strategy.getId())
            .orElseThrow(() -> new TradinException(ALREADY_SUBSCRIBED_EXCEPTION));
        validateAlreadyDeactivatedSubscription(subscription);
        deActivateSubscription(subscription);
    }

    private void validateAlreadyDeactivatedSubscription(Subscription subscription) {
        if (isAlreadyDeactivatedSubscription(subscription)) {
            throw new TradinException(ALREADY_SUBSCRIBED_EXCEPTION);
        }
    }

    private boolean isAlreadyDeactivatedSubscription(Subscription subscription) {
        return subscription.isDeActivated();
    }

    private void activateSubscriptionEntity(Account account, Strategy strategy) {
        subscriptionRepository.findByAccountIdAndStrategyId(account.getId(), strategy.getId())
            .ifPresentOrElse(
                subscription -> {
                    if (!subscription.isActivated()) {
                        subscription.activate();
                    }
                },
                () -> {
                    Subscription newSubscription = Subscription.of(account, strategy);
                    subscriptionRepository.save(newSubscription);
                }
            );
    }

    private void deActivateSubscription(Subscription subscription) {
        subscription.deActivate();
        subscriptionRepository.save(subscription);
    }
}
