package com.tradin.module.futures.position.domain;

import static java.math.BigDecimal.ONE;

import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.strategy.domain.TradingType;
import com.tradin.module.users.account.domain.Account;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "coinType"}))
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

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal entryPrice;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal liquidationPrice;

    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer leverage;

    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal margin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public FuturesPosition(CoinType coinType, TradingType tradingType, BigDecimal entryPrice, BigDecimal amount, Account account) {
        this.coinType = coinType;
        this.tradingType = tradingType;
        this.entryPrice = entryPrice;
        this.leverage = 1;
        this.liquidationPrice = calculateLiquidationPrice(tradingType, entryPrice, leverage);
        this.amount = amount;
        this.margin = amount.divide(BigDecimal.valueOf(leverage), 2, RoundingMode.CEILING);
        this.account = account;
    }

    private BigDecimal calculateLiquidationPrice(TradingType tradingType, BigDecimal entryPrice, Integer leverage) {
        return tradingType.isLong()
            ? entryPrice.multiply(ONE.subtract(ONE.divide(BigDecimal.valueOf(leverage), 2, RoundingMode.CEILING)))
            : entryPrice.multiply(ONE.add(ONE.divide(BigDecimal.valueOf(leverage), 2, RoundingMode.FLOOR)));
    }

    public static FuturesPosition of(CoinType coinType, TradingType tradingType, BigDecimal entryPrice, BigDecimal amount, Account account) {
        return FuturesPosition.builder()
            .coinType(coinType)
            .tradingType(tradingType)
            .entryPrice(entryPrice)
            .amount(amount)
            .account(account)
            .build();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isPositionLong() {
        return this.tradingType.isLong();
    }

    public boolean isPositionShort() {
        return this.tradingType.isShort();
    }
}
