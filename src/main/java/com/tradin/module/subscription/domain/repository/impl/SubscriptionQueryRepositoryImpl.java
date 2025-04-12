package com.tradin.module.subscription.domain.repository.impl;

import static com.tradin.module.subscription.domain.QSubscription.subscription;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.subscription.domain.Subscription;
import com.tradin.module.subscription.domain.repository.SubscriptionQueryRepository;
import com.tradin.module.subscription.domain.repository.dao.QSubscriptionsDao;
import com.tradin.module.subscription.domain.repository.dao.SubscriptionsDao;
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
}
