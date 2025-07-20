package com.tradin.core.subscription.implement;

import static com.tradin.core.common.exception.ExceptionType.NOT_SUBSCRIBED_STRATEGY_EXCEPTION;

import com.tradin.core.account.domain.Account;
import com.tradin.core.common.exception.TradinException;

import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.subscription.domain.Subscription;
import com.tradin.core.subscription.domain.repository.SubscriptionRepository;
import com.tradin.core.subscription.service.dto.FindSubscriptionsResponseDto;
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
