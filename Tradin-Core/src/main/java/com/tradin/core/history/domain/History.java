package com.tradin.core.history.domain;


import static com.tradin.core.strategy.domain.TradingType.LONG;

import com.tradin.core.common.jpa.AuditTime;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import com.tradin.core.common.converter.ProfitRateConverter;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(indexes = {
    @Index(name = "idx_history_strategy_id", columnList = "strategy_id"),
    @Index(name = "idx_history_created_at", columnList = "created_at"),
    @Index(name = "idx_history_strategy_created", columnList = "strategy_id, created_at")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History extends AuditTime {

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

    @Convert(converter = ProfitRateConverter.class)
    @Column(precision = 20, scale = 2)
    private ProfitRate profitRate;

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

    public void closeHistory(Position position) {
        this.exitPosition = position;
    }

    private boolean isOpenPositionLong() {
        return this.entryPosition.getTradingType() == LONG;
    }

    public void setProfitRate(ProfitRate profitRate) {
        this.profitRate = profitRate;
    }
}
