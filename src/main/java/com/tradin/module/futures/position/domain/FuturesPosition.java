package com.tradin.module.futures.position.domain;

import com.tradin.module.account.domain.Account;
import com.tradin.module.strategy.domain.CoinType;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FuturesPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType tradingType;

    @Column(nullable = false)
    private BigDecimal entryPrice;

    @Column(nullable = false)
    private BigDecimal liquidationPrice;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer leverage;

    @Column(nullable = false)
    private BigDecimal margin;

    @Column(nullable = false)
    private BigDecimal unrealizedPnl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public FuturesPosition(
        CoinType coinType,
        TradingType tradingType,
        BigDecimal entryPrice,
        BigDecimal liquidationPrice,
        BigDecimal amount,
        Integer leverage,
        BigDecimal margin,
        BigDecimal unrealizedPnl,
        Account account
    ) {
        this.coinType = coinType;
        this.tradingType = tradingType;
        this.entryPrice = entryPrice;
        this.liquidationPrice = liquidationPrice;
        this.amount = amount;
        this.leverage = leverage;
        this.margin = margin;
        this.unrealizedPnl = unrealizedPnl;
        this.account = account;
    }

    public static FuturesPosition of(
        CoinType coinType,
        TradingType tradingType,
        BigDecimal entryPrice,
        BigDecimal liquidationPrice,
        BigDecimal amount,
        Integer leverage,
        BigDecimal margin,
        BigDecimal unrealizedPnl,
        Account account
    ) {
        return FuturesPosition.builder()
            .coinType(coinType)
            .tradingType(tradingType)
            .entryPrice(entryPrice)
            .liquidationPrice(liquidationPrice)
            .amount(amount)
            .leverage(leverage)
            .margin(margin)
            .unrealizedPnl(unrealizedPnl)
            .account(account)
            .build();
    }
}
