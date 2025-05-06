package com.tradin.module.strategy.subscription.service;


import static com.tradin.common.exception.ExceptionType.ALREADY_POSITION_EXIST_EXCEPTION;
import static com.tradin.common.exception.ExceptionType.ALREADY_SUBSCRIBED_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.futures.position.implement.FuturesPositionReader;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.strategy.implement.StrategyReader;
import com.tradin.module.strategy.subscription.controller.dto.FindSubscriptionsResponseDto;
import com.tradin.module.strategy.subscription.domain.Subscription;
import com.tradin.module.strategy.subscription.implement.SubscriptionProcessor;
import com.tradin.module.strategy.subscription.implement.SubscriptionReader;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.implement.AccountReader;
import java.util.List;
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
    private final FuturesPositionReader futuresPositionReader;

    public FindSubscriptionsResponseDto findSubscriptions(Long userId, Long accountId) {
        validateExistAccount(userId, accountId);
        return subscriptionReader.findAll(accountId);
    }

    @Transactional
    public void activateSubscription(Long userId, Long accountId, Long strategyId) {
        Account account = validateExistAccount(userId, accountId);
        Strategy strategy = validateExistStrategy(strategyId);
        validateExistActiveSubscriptionByAccountIdAndCoinType(account.getId(), strategy.getCoinType());
        validateExistPosition(accountId, strategy.getCoinType());
        activateSubscription(account, strategy);
    }

    @Transactional
    public void deActivateSubscription(Long userId, Long accountId, Long strategyId) {
        Account account = validateExistAccount(userId, accountId);
        Strategy strategy = validateExistStrategy(strategyId);
        validateExistPosition(accountId, strategy.getCoinType());
        deActivateSubscription(account, strategy);
    }


    @Transactional
    public void activateSubscriptionTest(Long userId, Long strategyId) {
        List<Account> account = accountReader.findAll();
        Strategy strategy = validateExistStrategy(strategyId);
        for (Account acc : account) {
            activateSubscription(acc, strategy);
        }
    }

    @Transactional
    public void deActivateSubscriptionTest(Long userId, Long strategyId) {
        List<Account> account = accountReader.findAll();
        Strategy strategy = validateExistStrategy(strategyId);

        for (Account acc : account) {
            deActivateSubscription(acc, strategy);
        }
    }

    private void validateExistActiveSubscriptionByAccountIdAndCoinType(Long accountId, CoinType coinType) {
        subscriptionReader.findByAccountIdAndCoinTypeOptional(accountId, coinType).ifPresent(subscription -> {
            if (!subscription.isDeActivated()) {
                throw new TradinException(ALREADY_SUBSCRIBED_EXCEPTION);
            }
        });
    }

    private void deActivateSubscription(Account account, Strategy strategy) {
        Subscription subscription = subscriptionReader.findByAccountIdAndStrategyId(account.getId(), strategy.getId());
        validateAlreadyDeactivatedSubscription(subscription); //TODO - bad smell
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

    private Account validateExistAccount(Long userId, Long accountId) { //TODO - validation도 Reader로 분리
        return accountReader.findAccountByIdAndUserId(accountId, userId);
    }

    private Strategy validateExistStrategy(Long strategyId) {
        return strategyReader.findStrategyById(strategyId);
    }

    private void activateSubscription(Account account, Strategy strategy) {
        subscriptionReader.findByAccountIdAndStrategyIdOptional(account.getId(), strategy.getId())
            .ifPresentOrElse(
                subscription -> {
                    if (!subscription.isActivated()) {
                        subscriptionProcessor.activateSubscription(subscription);
                    }
                },
                () -> subscriptionProcessor.createSubscription(account, strategy)
            );
    }


    private void validateExistPosition(Long accountId, CoinType coinType) {
        futuresPositionReader.findOpenFuturesPositionByAccountAndCoinTypeForUpdate(accountId, coinType)
            .ifPresent(futuresPosition -> {
                throw new TradinException(ALREADY_POSITION_EXIST_EXCEPTION);
            });
    }

    private void deActivateSubscription(Subscription subscription) {
        subscriptionProcessor.deActivateSubscription(subscription);
    }
}
