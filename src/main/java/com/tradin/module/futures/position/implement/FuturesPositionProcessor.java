package com.tradin.module.futures.position.implement;

import com.tradin.module.futures.position.domain.FuturesPosition;
import com.tradin.module.futures.position.domain.repository.FuturesPositionRepository;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.strategy.domain.TradingType;
import com.tradin.module.users.account.domain.Account;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FuturesPositionProcessor {

    private final FuturesPositionRepository futuresPositionRepository;
    private final EntityManager entityManager;

    public void closePosition(Account account, FuturesPosition futuresPosition) {
        futuresPositionRepository.delete(futuresPosition);
        futuresPositionRepository.flush(); //TODO - 추후 구조 개선을 통해 삭제

    }

    public FuturesPosition openPosition(CoinType coinType, TradingType tradingType, BigDecimal amount, BigDecimal price, Account account) {
        FuturesPosition futuresPosition = FuturesPosition.of(coinType, tradingType, price, amount, account);
        return futuresPositionRepository.save(futuresPosition);
    }

}
