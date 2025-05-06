package com.tradin.module.strategy.subscription.domain.repository;

import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.subscription.domain.Subscription;
import com.tradin.module.strategy.subscription.domain.repository.dao.SubscriptionsDao;
import com.tradin.module.users.account.domain.Account;
import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryRepository {

    List<SubscriptionsDao> findAllByUserIdAndAccountId(Long accountId);

    Optional<Subscription> findByAccountIdAndStrategyId(Long accountId, Long strategyId);

    Optional<Subscription> findSubscriptionByAccountIdAndCoinType(Long accountId, CoinType coinType);

    List<Account> findSubscribedAccountsByStrategyId(Long strategyId);
}
