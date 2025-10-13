package com.tradin.core.futuresPosition.domain.repository.impl;


import static com.tradin.core.futuresPosition.domain.QFuturesPosition.futuresPosition;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.futuresPosition.domain.repository.FuturesPositionQueryRepository;
import com.tradin.core.futuresPosition.domain.repository.dao.FuturesPositionDao;
import com.tradin.core.futuresPosition.domain.repository.dao.QFuturesPositionDao;
import com.tradin.core.strategy.domain.CoinType;
import java.util.List;
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

    public List<FuturesPositionDao> findOpenFuturesPositionDaosByAccountId(Long accountId) {
        return jpaQueryFactory
            .select(new QFuturesPositionDao(
                futuresPosition.id,
                futuresPosition.coinType,
                futuresPosition.tradingType,
                futuresPosition.entryPrice,
                futuresPosition.liquidationPrice,
                futuresPosition.amount,
                futuresPosition.leverage,
                futuresPosition.margin
            ))
            .from(futuresPosition)
            .where(futuresPosition.account.id.eq(accountId))
            .fetch();
    }
}
