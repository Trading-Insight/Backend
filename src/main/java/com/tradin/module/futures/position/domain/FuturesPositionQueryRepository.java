package com.tradin.module.futures.position.domain;

import com.tradin.module.strategy.strategy.domain.CoinType;
import java.util.Optional;

public interface FuturesPositionQueryRepository {

    Optional<FuturesPosition> findOpenFuturesPositionByAccountAndCoinTypeForUpdate(Long accountId, CoinType coinType);

}
