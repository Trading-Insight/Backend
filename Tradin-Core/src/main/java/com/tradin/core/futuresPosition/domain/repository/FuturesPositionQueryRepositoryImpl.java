package com.tradin.core.futuresPosition.domain.repository;


import static com.tradin.core.futuresPosition.domain.QFuturesPosition.futuresPosition;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.futuresPosition.domain.FuturesPositionQueryRepository;
import com.tradin.core.strategy.domain.CoinType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FuturesPositionQueryRepositoryImpl implements FuturesPositionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<FuturesPosition> findOpenFuturesPositionByAccountAndCoinType(Long accountId, CoinType coinType) {
        return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(futuresPosition)
                .where(
                    futuresPosition.account.id.eq(accountId),
                    futuresPosition.coinType.eq(coinType)
                )
                .fetchOne());
    }
}
