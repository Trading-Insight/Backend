package com.tradin.module.futures.order.domain;

import com.tradin.common.jpa.AuditTime;
import com.tradin.module.account.domain.Account;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.strategy.domain.TradingType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FuturesOrder extends AuditTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType tradingType;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer leverage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id", nullable = false)
    private Strategy strategy;


    @Builder
    public FuturesOrder(TradingType tradingType, BigDecimal price, BigDecimal amount, Integer leverage, OrderStatus orderStatus, Account account, Strategy strategy) {
        this.tradingType = tradingType;
        this.price = price;
        this.amount = amount;
        this.leverage = leverage;
        this.orderStatus = orderStatus;
        this.account = account;
        this.strategy = strategy;
    }

    public static FuturesOrder of(TradingType tradingType, BigDecimal price, BigDecimal amount, Integer leverage, OrderStatus orderStatus, Account account, Strategy strategy) {
        return FuturesOrder.builder()
            .tradingType(tradingType)
            .price(price)
            .amount(amount)
            .leverage(leverage)
            .orderStatus(orderStatus)
            .account(account)
            .strategy(strategy)
            .build();
    }

}
