package com.tradin.module.futures.position.implement;

import com.tradin.module.futures.position.domain.FuturesPosition;
import com.tradin.module.futures.position.domain.repository.FuturesPositionRepository;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.strategy.domain.TradingType;
import com.tradin.module.users.account.domain.Account;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FuturesPositionProcessor {

    private final FuturesPositionRepository futuresPositionRepository;

    public void closePosition(Account account, FuturesPosition futuresPosition) {
        account.removeFuturesPosition(futuresPosition);
        futuresPositionRepository.delete(futuresPosition);
        futuresPositionRepository.flush(); //TODO - 추후 구조 개선을 통해 삭제

    }

    public FuturesPosition openPosition(CoinType coinType, TradingType tradingType, BigDecimal amount, BigDecimal price, Account account) {
        FuturesPosition futuresPosition = FuturesPosition.of(coinType, tradingType, price, amount, account);
        account.addFuturesPosition(futuresPosition);
        return futuresPositionRepository.save(futuresPosition);
    }

}
