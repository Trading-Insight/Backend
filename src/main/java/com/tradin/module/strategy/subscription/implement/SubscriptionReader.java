package com.tradin.module.strategy.subscription.implement;

import static com.tradin.common.exception.ExceptionType.NOT_SUBSCRIBED_STRATEGY_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.subscription.controller.dto.FindSubscriptionsResponseDto;
import com.tradin.module.strategy.subscription.domain.Subscription;
import com.tradin.module.strategy.subscription.domain.repository.SubscriptionRepository;
import com.tradin.module.users.account.domain.Account;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionReader {

    private final SubscriptionRepository subscriptionRepository;

    public FindSubscriptionsResponseDto findAll(Long accountId) {
        return FindSubscriptionsResponseDto.of(subscriptionRepository.findAllByUserIdAndAccountId(accountId));
    }

//    public Optional<Subscription> findByAccountIdAndStrategyIdOptional(Long accountId, Long strategyId) {
//        return subscriptionRepository.findByAccountIdAndStrategyId(accountId, strategyId);
//    }

    public Subscription findByAccountIdAndStrategyId(Long accountId, Long strategyId) {
        return subscriptionRepository.findByAccountIdAndStrategyId(accountId, strategyId)
            .orElseThrow(() -> new TradinException(NOT_SUBSCRIBED_STRATEGY_EXCEPTION));
    }

    public Optional<Subscription> findByAccountIdAndStrategyIdOptional(Long accountId, Long strategyId) {
        return subscriptionRepository.findByAccountIdAndStrategyId(accountId, strategyId);
    }

    public Optional<Subscription> findByAccountIdAndCoinTypeOptional(Long accountId, CoinType coinType) {
        return subscriptionRepository.findSubscriptionByAccountIdAndCoinType(accountId, coinType);
    }

    public List<Account> findSubscribedAccountsByStrategyId(Long strategyId) {
        return subscriptionRepository.findSubscribedAccountsByStrategyId(strategyId);
    }

}
