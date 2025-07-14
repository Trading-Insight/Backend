package com.tradin.module.users.account.domain.repository.impl;

import static com.tradin.module.strategy.subscription.domain.QSubscription.subscription;
import static com.tradin.module.strategy.subscription.domain.SubscriptionStatus.ACTIVE;
import static com.tradin.module.users.account.domain.QAccount.account;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.domain.repository.AccountQueryRepository;
import com.tradin.module.users.account.service.dto.AccountDto;
import com.tradin.module.users.account.service.dto.QAccountDto;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountQueryRepositoryImpl implements AccountQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<AccountDto> findAccountDtosByUserId(Long userId) {
        return jpaQueryFactory
            .select(new QAccountDto(
                account.id,
                account.name
            ))
            .from(account)
            .where(account.user.id.eq(userId))
            .fetch();
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
