package com.tradin.core.futuresOrder.domain;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.common.jpa.AuditTime;

import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.futuresOrder.domain.vo.Margin;
import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.common.converter.AmountConverter;
import com.tradin.core.common.converter.MarginConverter;
import com.tradin.core.common.converter.PriceConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import java.math.RoundingMode;
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

    @Convert(converter = PriceConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private Price price;

    @Convert(converter = AmountConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private Amount amount;

    @Column(nullable = false)
    private Integer leverage;

    @Convert(converter = MarginConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private Margin margin;

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
    public FuturesOrder(TradingType tradingType, Price price, Amount amount, OrderStatus orderStatus, Account account, Strategy strategy) {
        this.tradingType = tradingType;
        this.price = price;
        this.amount = amount;
        this.leverage = 1;
        this.margin = Margin.of(amount.getValue().divide(BigDecimal.valueOf(leverage), 2, RoundingMode.CEILING));
        this.orderStatus = orderStatus;
        this.account = account;
        this.strategy = strategy;
    }

    public static FuturesOrder of(TradingType tradingType, Price price, Amount amount, OrderStatus orderStatus, Account account, Strategy strategy) {
        return FuturesOrder.builder()
            .tradingType(tradingType)
            .price(price)
            .amount(amount)
            .orderStatus(orderStatus)
            .account(account)
            .strategy(strategy)
            .build();
    }


}
