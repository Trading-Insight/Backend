package com.tradin.core.futuresPosition.domain;


import com.tradin.core.strategy.domain.CoinType;
import java.util.Optional;

public interface FuturesPositionQueryRepository {

    Optional<FuturesPosition> findOpenFuturesPositionByAccountAndCoinType(Long accountId, CoinType coinType);

}
