package com.tradin.module.futures.order.event;

import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.users.account.domain.Account;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AutoTradeEvent extends ApplicationEvent {

    private final Strategy strategy;
    private final Account account;
    private final Position position;

    public AutoTradeEvent(Object source, Strategy strategy, Account account, Position position) {
        super(source);
        this.strategy = strategy;
        this.account = account;
        this.position = position;
    }
}
