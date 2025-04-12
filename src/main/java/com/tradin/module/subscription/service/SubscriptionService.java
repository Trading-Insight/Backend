package com.tradin.module.subscription.service;


import static com.tradin.common.exception.ExceptionType.ALREADY_SUBSCRIBED_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.account.domain.Account;
import com.tradin.module.account.implement.AccountReader;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.strategy.implement.StrategyReader;
import com.tradin.module.subscription.controller.dto.FindSubscriptionsResponseDto;
import com.tradin.module.subscription.domain.Subscription;
import com.tradin.module.subscription.implement.SubscriptionProcessor;
import com.tradin.module.subscription.implement.SubscriptionReader;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionReader subscriptionReader;
    private final SubscriptionProcessor subscriptionProcessor;
    private final AccountReader accountReader;
    private final StrategyReader strategyReader;

    public FindSubscriptionsResponseDto findSubscriptions(Long userId, Long accountId) {
        validateExistAccount(userId, accountId);
        return subscriptionReader.findSubscriptions(accountId);
    }

    @Transactional
    public void activateSubscription(Long userId, Long accountId, Long strategyId) {
        //validateExistPosition() TODO - 오픈 포지션이 있는지 체크
        Account account = validateExistAccount(userId, accountId);
        Strategy strategy = validateExistStrategy(strategyId);
        activateSubscription(account, strategy);
    }

    @Transactional
    public void deActivateSubscription(Long userId, Long accountId, Long strategyId) {
        Account account = validateExistAccount(userId, accountId);
        Strategy strategy = validateExistStrategy(strategyId);
        deActivateSubscription(account, strategy);
    }

    private void deActivateSubscription(Account account, Strategy strategy) {
        Subscription subscription = subscriptionReader.findByAccountIdAndStrategyId(account.getId(), strategy.getId());
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

    private Account validateExistAccount(Long userId, Long accountId) {
        return accountReader.findAccountByIdAndUserId(accountId, userId);
    }

    private Strategy validateExistStrategy(Long strategyId) {
        return strategyReader.findStrategyById(strategyId);
    }

    private void activateSubscription(Account account, Strategy strategy) {
        Optional<Subscription> subscriptionOptional = subscriptionReader.findByAccountIdAndStrategyIdOptional(
            account.getId(),
            strategy.getId()
        );

        subscriptionOptional.ifPresentOrElse(this::activateIfAlreadyExist, () -> createSubscription(account, strategy));
    }

    private void activateIfAlreadyExist(Subscription subscription) {
        if (isAlreadyActivatedSubscription(subscription)) {
            throw new TradinException(ALREADY_SUBSCRIBED_EXCEPTION);
        }
        activateSubscription(subscription);
    }

    private static boolean isAlreadyActivatedSubscription(Subscription subscription) {
        return subscription.isActivated();
    }

    private void activateSubscription(Subscription subscription) {
        subscriptionProcessor.activateSubscription(subscription);
    }

    private void createSubscription(Account account, Strategy strategy) {
        subscriptionProcessor.createSubscription(account, strategy);
    }

    private void deActivateSubscription(Subscription subscription) {
        subscriptionProcessor.deActivateSubscription(subscription);
    }
}
