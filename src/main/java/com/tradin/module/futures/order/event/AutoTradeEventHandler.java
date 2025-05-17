package com.tradin.module.futures.order.event;

import com.tradin.module.futures.order.implement.FuturesOrderProcessor;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.users.account.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoTradeEventHandler {

    private final FuturesOrderProcessor futuresOrderProcessor;

    @Async("tradeExecutor")
    @EventListener(AutoTradeEvent.class)
    public void handleAutoTradeEvent(AutoTradeEvent event) {
        Strategy strategy = event.getStrategy();
        Account account = event.getAccount();
        Position position = event.getPosition();

        try {
            futuresOrderProcessor.closeExistPosition(strategy, account);
            futuresOrderProcessor.openNewPosition(strategy, account, position);
        } catch (Exception e) {
            log.warn("❌ 자동매매 실패 - accountId={}, strategyId={}, error={}", account.getId(), strategy.getId(), e.getMessage(), e);
        }
    }
}
