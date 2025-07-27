package com.tradin.core.futuresPosition.domain.repository;


import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.strategy.domain.CoinType;
import java.util.Optional;

public interface FuturesPositionQueryRepository {

    Optional<FuturesPosition> findOpenFuturesPositionByAccountAndCoinType(Long accountId, CoinType coinType);

}
