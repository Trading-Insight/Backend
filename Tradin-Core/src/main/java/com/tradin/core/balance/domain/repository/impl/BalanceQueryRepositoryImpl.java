package com.tradin.core.balance.domain.repository.impl;


import static com.tradin.core.balance.domain.QBalance.balance;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.repository.BalanceQueryRepository;
import com.tradin.core.strategy.domain.CoinType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BalanceQueryRepositoryImpl implements BalanceQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Balance> findByAccountIdAndCoinType(Long accountId, CoinType coinType) {
        return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(balance)
                .where(
                    balance.account.id.eq(accountId),
                    balance.coinType.eq(coinType)
                )
                .fetchOne()
        );
    }
}
