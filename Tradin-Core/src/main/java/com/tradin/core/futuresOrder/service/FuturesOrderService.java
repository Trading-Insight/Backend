package com.tradin.core.futuresOrder.service;


import com.tradin.core.account.domain.Account;
import com.tradin.core.futuresOrder.implement.FuturesOrderProcessor;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class FuturesOrderService {

    private final FuturesOrderProcessor futuresOrderProcessor;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void autoTrade(Strategy strategy, Account account, Position position) {
        futuresOrderProcessor.closeExistPosition(strategy, account);
        futuresOrderProcessor.openNewPosition(strategy, account, position);
    }

}
