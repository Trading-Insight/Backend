package com.tradin.core.futuresPosition.domain;

import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.common.jpa.AuditTime;

import com.tradin.core.account.domain.Account;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.common.converter.AmountConverter;
import com.tradin.core.futuresOrder.domain.vo.Margin;
import com.tradin.core.common.converter.MarginConverter;
import com.tradin.core.price.domain.vo.Price;
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
import jakarta.persistence.Index;
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
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "coinType"}),
    indexes = {
        @Index(name = "idx_futures_position_account_coin", columnList = "account_id, coinType"),
        @Index(name = "idx_futures_position_created_at", columnList = "created_at")
    }
)
public class FuturesPosition extends AuditTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType tradingType;

    @Convert(converter = PriceConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private Price entryPrice;

    @Convert(converter = PriceConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private Price liquidationPrice;

    @Convert(converter = AmountConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private Amount amount;

    @Column(nullable = false)
    private Integer leverage;

    @Convert(converter = MarginConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private Margin margin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public FuturesPosition(CoinType coinType, TradingType tradingType, Price entryPrice, Amount amount, Account account) {
        this.coinType = coinType;
        this.tradingType = tradingType;
        this.entryPrice = entryPrice;
        this.leverage = 1;
        this.liquidationPrice = calculateLiquidationPrice(tradingType, entryPrice, leverage);
        this.amount = amount;
        this.margin = Margin.of(amount.getValue().divide(BigDecimal.valueOf(leverage), 2, RoundingMode.CEILING));
        this.account = account;
    }

    private Price calculateLiquidationPrice(TradingType tradingType, Price entryPrice, Integer leverage) {
        BigDecimal entry = entryPrice.getValue();
        BigDecimal result = tradingType.isLong()
            ? entry.multiply(BigDecimal.ONE.subtract(BigDecimal.ONE.divide(BigDecimal.valueOf(leverage), 2, RoundingMode.CEILING)))
            : entry.multiply(BigDecimal.ONE.add(BigDecimal.ONE.divide(BigDecimal.valueOf(leverage), 2, RoundingMode.FLOOR)));
        return Price.of(result);
    }

    public static FuturesPosition of(CoinType coinType, TradingType tradingType, Price entryPrice, Amount amount, Account account) {
        return FuturesPosition.builder()
            .coinType(coinType)
            .tradingType(tradingType)
            .entryPrice(entryPrice)
            .amount(amount)
            .account(account)
            .build();
    }

    public boolean isPositionLong() {
        return this.tradingType.isLong();
    }

    public boolean isPositionShort() {
        return this.tradingType.isShort();
    }

    public Amount calculateProfitAmount(Price currentPrice) {
        BigDecimal entryPriceValue = this.entryPrice.getValue();
        BigDecimal currentPriceValue = currentPrice.getValue();
        BigDecimal amountValue = this.amount.getValue();

        if (this.tradingType.isLong()) {
            return Amount.of(currentPriceValue.subtract(entryPriceValue).multiply(amountValue).divide(entryPriceValue, 2, RoundingMode.DOWN));
        }

        return Amount.of(entryPriceValue.subtract(currentPriceValue).multiply(amountValue).divide(entryPriceValue, 2, RoundingMode.DOWN));
    }
}
