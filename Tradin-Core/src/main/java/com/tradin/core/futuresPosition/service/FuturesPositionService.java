package com.tradin.core.futuresPosition.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.futuresPosition.domain.repository.FuturesPositionRepository;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.price.domain.vo.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FuturesPositionService {
    private final FuturesPositionRepository futuresPositionRepository;

    public void closePosition(FuturesPosition futuresPosition) {
        futuresPositionRepository.delete(futuresPosition);
        futuresPositionRepository.flush();
    }

    public FuturesPosition openPosition(CoinType coinType, TradingType tradingType, Amount amount, Price price, Account account) {
        FuturesPosition futuresPosition = FuturesPosition.of(coinType, tradingType, price, amount, account);
        return futuresPositionRepository.save(futuresPosition);
    }

    public Optional<FuturesPosition> findOpenFuturesPositionByAccountAndCoinType(Long accountId, CoinType coinType) {
        return futuresPositionRepository.findOpenFuturesPositionByAccountAndCoinType(accountId, coinType);
    }
}
