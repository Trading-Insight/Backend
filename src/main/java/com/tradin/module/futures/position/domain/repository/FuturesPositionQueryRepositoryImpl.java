package com.tradin.module.futures.position.domain.repository;

import static com.tradin.module.futures.position.domain.QFuturesPosition.futuresPosition;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.futures.position.domain.FuturesPosition;
import com.tradin.module.futures.position.domain.FuturesPositionQueryRepository;
import com.tradin.module.strategy.strategy.domain.CoinType;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FuturesPositionQueryRepositoryImpl implements FuturesPositionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<FuturesPosition> findOpenFuturesPositionByAccountAndCoinTypeForUpdate(Long accountId, CoinType coinType) {
        return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(futuresPosition)
                .where(
                    futuresPosition.account.id.eq(accountId),
                    futuresPosition.coinType.eq(coinType)
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
