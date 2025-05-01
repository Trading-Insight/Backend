package com.tradin.module.strategy.history.domain;

import static com.tradin.module.strategy.strategy.domain.TradingType.LONG;

import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(indexes = {@Index(name = "index_strategy_id", columnList = "strategy_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "tradingType", column = @Column(name = "entry_trading_type"))
    @AttributeOverride(name = "time", column = @Column(name = "entry_time"))
    @AttributeOverride(name = "price", column = @Column(name = "entry_price"))
    private Position entryPosition;

    @Embedded
    @AttributeOverride(name = "tradingType", column = @Column(name = "exit_trading_type"))
    @AttributeOverride(name = "time", column = @Column(name = "exit_time"))
    @AttributeOverride(name = "price", column = @Column(name = "exit_price"))
    private Position exitPosition;

    @Column
    private Double profitRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id", nullable = false)
    private Strategy strategy;

    @Builder
    private History(Position entryPosition, Strategy strategy) {
        this.entryPosition = entryPosition;
        this.exitPosition = null;
        this.profitRate = null;
        this.strategy = strategy;
    }

    public static History of(Position entryPosition, Strategy strategy) {
        return History.builder()
            .entryPosition(entryPosition)
            .strategy(strategy)
            .build();
    }

    public void closeOpenPosition(Position position) {
        this.exitPosition = position;
    }

//    public void calculateProfitRate() {
//        if (isOpenPositionLong()) {
//            this.profitRate =
//                    (double) (this.exitPosition.getPrice() - this.entryPosition.getPrice()) / this.entryPosition.getPrice()
//                            * 100;
//        } else {
//            this.profitRate =
//                    (double) (this.entryPosition.getPrice() - this.exitPosition.getPrice()) / this.entryPosition.getPrice()
//                            * 100;
//        }
//    }

    private boolean isOpenPositionLong() {
        return this.entryPosition.getTradingType() == LONG;
    }
}
