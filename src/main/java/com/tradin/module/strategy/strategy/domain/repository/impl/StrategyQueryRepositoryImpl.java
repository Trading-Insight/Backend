package com.tradin.module.strategy.strategy.domain.repository.impl;

import static com.tradin.module.strategy.strategy.domain.QStrategy.strategy;
import static com.tradin.module.strategy.strategy.domain.StrategyType.FUTURE;
import static com.tradin.module.strategy.strategy.domain.StrategyType.SPOT;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.strategy.strategy.domain.StrategyType;
import com.tradin.module.strategy.strategy.domain.repository.StrategyQueryRepository;
import com.tradin.module.strategy.strategy.domain.repository.dao.QStrategyInfoDao;
import com.tradin.module.strategy.strategy.domain.repository.dao.QSubscriptionStrategyInfoDao;
import com.tradin.module.strategy.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.strategy.domain.repository.dao.SubscriptionStrategyInfoDao;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StrategyQueryRepositoryImpl implements StrategyQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StrategyInfoDao> findAllFutureStrategiesInfoDao() {
        return getStrategyInfoDaos(FUTURE);
    }

    @Override
    public List<StrategyInfoDao> findAllSpotStrategiesInfoDao() {
        return getStrategyInfoDaos(SPOT);
    }

    @Override
    public Optional<StrategyInfoDao> findStrategyInfoDaoById(Long id) {
        return Optional.ofNullable(jpaQueryFactory
            .select(
                new QStrategyInfoDao(
                    strategy.id,
                    strategy.name,
                    strategy.type.coinType,
                    strategy.profitFactor,
                    strategy.rate.winningRate,
                    strategy.rate.simpleProfitRate,
                    strategy.rate.compoundProfitRate,
                    strategy.rate.totalProfitRate,
                    strategy.rate.totalLossRate,
                    strategy.rate.averageProfitRate,
                    strategy.count.totalTradeCount,
                    strategy.count.winCount,
                    strategy.count.lossCount,
                    strategy.currentPosition.tradingType,
                    strategy.currentPosition.time,
                    strategy.currentPosition.price,
                    strategy.averageHoldingPeriod
                ))
            .from(strategy)
            .where(strategy.id.eq(id))
            .fetchOne());
    }

    private List<StrategyInfoDao> getStrategyInfoDaos(StrategyType strategyType) {
        return jpaQueryFactory
            .select(
                new QStrategyInfoDao(
                    strategy.id,
                    strategy.name,
                    strategy.type.coinType,
                    strategy.profitFactor,
                    strategy.rate.winningRate,
                    strategy.rate.simpleProfitRate,
                    strategy.rate.compoundProfitRate,
                    strategy.rate.totalProfitRate,
                    strategy.rate.totalLossRate,
                    strategy.rate.averageProfitRate,
                    strategy.count.totalTradeCount,
                    strategy.count.winCount,
                    strategy.count.lossCount,
                    strategy.currentPosition.tradingType,
                    strategy.currentPosition.time,
                    strategy.currentPosition.price,
                    strategy.averageHoldingPeriod
                ))
            .from(strategy)
            .where(strategy.type.strategyType.eq(strategyType))
            .orderBy(strategy.id.asc())
            .fetch();
    }

    @Override
    public Optional<List<SubscriptionStrategyInfoDao>> findSubscriptionStrategiesInfoDao() {
        List<SubscriptionStrategyInfoDao> subscriptionStrategyInfoDaos = jpaQueryFactory
            .select(
                new QSubscriptionStrategyInfoDao(
                    strategy.id,
                    strategy.name,
                    strategy.type.coinType,
                    strategy.profitFactor,
                    strategy.rate.winningRate,
                    strategy.rate.compoundProfitRate,
                    strategy.rate.averageProfitRate
                ))
            .from(strategy)
            .where(strategy.type.strategyType.eq(FUTURE))
            .orderBy(strategy.id.asc())
            .fetch();

        return subscriptionStrategyInfoDaos.isEmpty() ? Optional.empty()
            : Optional.of(subscriptionStrategyInfoDaos);
    }
}
