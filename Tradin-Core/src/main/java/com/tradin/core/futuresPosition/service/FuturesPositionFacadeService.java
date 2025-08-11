package com.tradin.core.futuresPosition.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.common.annotation.DistributedLock;
import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.price.domain.vo.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FuturesPositionFacadeService {
    private final FuturesPositionService futuresPositionService;

    @Transactional(readOnly = true)
    public Optional<FuturesPosition> findOpenFuturesPositionByAccountAndCoinType(Long accountId, CoinType coinType) {
        return futuresPositionService.findOpenFuturesPositionByAccountAndCoinType(accountId, coinType);
    }
}
