package com.tradin.module.subscription.domain.repository;

import com.tradin.module.strategy.domain.CoinType;
import com.tradin.module.subscription.domain.Subscription;
import com.tradin.module.subscription.domain.repository.dao.SubscriptionsDao;
import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryRepository {

    List<SubscriptionsDao> findAllByUserIdAndAccountId(Long accountId);

    Optional<Subscription> findByAccountIdAndStrategyId(Long accountId, Long strategyId);

    Boolean isExistCoinTypeSubscriptionByAccountIdAndCoinType(Long accountId, CoinType coinType);
}
