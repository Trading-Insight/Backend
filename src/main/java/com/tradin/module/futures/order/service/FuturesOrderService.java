package com.tradin.module.futures.order.service;

import com.tradin.module.futures.order.implement.FuturesOrderProcessor;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.users.account.domain.Account;
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
