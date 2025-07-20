package com.tradin.core.subscription.domain.repository;


import com.tradin.core.account.domain.Account;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.subscription.domain.Subscription;
import com.tradin.core.subscription.domain.repository.dao.SubscriptionsDao;
import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryRepository {

    List<SubscriptionsDao> findAllByUserIdAndAccountId(Long accountId);

    Optional<Subscription> findByAccountIdAndStrategyId(Long accountId, Long strategyId);

    Optional<Subscription> findSubscriptionByAccountIdAndCoinType(Long accountId, CoinType coinType);

    List<Account> findSubscribedAccountsByStrategyId(Long strategyId);
}
