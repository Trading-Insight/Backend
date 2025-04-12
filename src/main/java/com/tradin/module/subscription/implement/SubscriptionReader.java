package com.tradin.module.subscription.implement;

import static com.tradin.common.exception.ExceptionType.NOT_SUBSCRIBED_STRATEGY_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.strategy.domain.CoinType;
import com.tradin.module.subscription.controller.dto.FindSubscriptionsResponseDto;
import com.tradin.module.subscription.domain.Subscription;
import com.tradin.module.subscription.domain.repository.SubscriptionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionReader {

    private final SubscriptionRepository subscriptionRepository;

    public FindSubscriptionsResponseDto findSubscriptions(Long accountId) {
        return FindSubscriptionsResponseDto.of(subscriptionRepository.findAllByUserIdAndAccountId(accountId));
    }

    public Optional<Subscription> findByAccountIdAndStrategyIdOptional(Long accountId, Long strategyId) {
        return subscriptionRepository.findByAccountIdAndStrategyId(accountId, strategyId);
    }

    public Subscription findByAccountIdAndStrategyId(Long accountId, Long strategyId) {
        return subscriptionRepository.findByAccountIdAndStrategyId(accountId, strategyId)
            .orElseThrow(() -> new TradinException(NOT_SUBSCRIBED_STRATEGY_EXCEPTION));
    }

    public Boolean isExistCoinTypeSubscriptionByAccountIdAndCoinType(Long accountId, CoinType coinType) {
        return subscriptionRepository.isExistCoinTypeSubscriptionByAccountIdAndCoinType(accountId, coinType);
    }

}
