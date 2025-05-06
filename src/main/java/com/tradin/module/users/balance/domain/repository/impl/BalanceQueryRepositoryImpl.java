package com.tradin.module.users.balance.domain.repository.impl;

import static com.tradin.module.users.balance.domain.QBalance.balance;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.users.balance.domain.Balance;
import com.tradin.module.users.balance.domain.repository.BalanceQueryRepository;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BalanceQueryRepositoryImpl implements BalanceQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Balance> findByAccountIdAndCoinTypeForUpdate(Long accountId, CoinType coinType) {
        return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(balance)
                .where(
                    balance.account.id.eq(accountId),
                    balance.coinType.eq(coinType)
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne()
        );
    }
}
