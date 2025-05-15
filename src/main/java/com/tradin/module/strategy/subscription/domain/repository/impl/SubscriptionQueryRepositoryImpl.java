package com.tradin.module.strategy.subscription.domain.repository.impl;

import static com.tradin.module.strategy.strategy.domain.QStrategy.strategy;
import static com.tradin.module.strategy.subscription.domain.QSubscription.subscription;
import static com.tradin.module.strategy.subscription.domain.SubscriptionStatus.ACTIVE;
import static com.tradin.module.users.account.domain.QAccount.account;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.subscription.domain.Subscription;
import com.tradin.module.strategy.subscription.domain.repository.SubscriptionQueryRepository;
import com.tradin.module.strategy.subscription.domain.repository.dao.QSubscriptionsDao;
import com.tradin.module.strategy.subscription.domain.repository.dao.SubscriptionsDao;
import com.tradin.module.users.account.domain.Account;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubscriptionQueryRepositoryImpl implements SubscriptionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SubscriptionsDao> findAllByUserIdAndAccountId(Long accountId) {
        return jpaQueryFactory.select(new QSubscriptionsDao(
                subscription.id,
                subscription.account.id,
                subscription.strategy.id,
                subscription.strategy.name,
                subscription.strategy.type,
                subscription.startDate,
                subscription.endDate,
                subscription.status
            ))
            .from(subscription)
            .join(subscription.strategy, strategy)
            .where(subscription.account.id.eq(accountId))
            .fetch();
    }

    @Override
    public Optional<Subscription> findByAccountIdAndStrategyId(Long accountId, Long strategyId) {
        return Optional.ofNullable(
            jpaQueryFactory.selectFrom(subscription)
                .where(subscription.account.id.eq(accountId)
                    .and(subscription.strategy.id.eq(strategyId)))
                .fetchOne()
        );
    }

    @Override
    public Optional<Subscription> findSubscriptionByAccountIdAndCoinType(Long accountId, CoinType coinType) {
        return Optional.ofNullable(
            jpaQueryFactory.selectFrom(subscription)
                .where(subscription.account.id.eq(accountId)
                    .and(subscription.strategy.type.coinType.eq(coinType)))
                .fetchOne()
        );
    }

    @Override
    public List<Account> findSubscribedAccountsByStrategyId(Long strategyId) {
        return jpaQueryFactory
            .select(account)
            .from(subscription)
            .join(subscription.account, account)
            .where(subscription.strategy.id.eq(strategyId)
                .and(account.isDeleted.isFalse())
                .and(subscription.status.eq(ACTIVE)))
            .fetch();

    }
}
