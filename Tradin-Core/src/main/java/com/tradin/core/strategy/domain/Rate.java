package com.tradin.core.strategy.domain;

import com.tradin.core.common.converter.ProfitRateConverter;
import com.tradin.core.common.converter.WinRateConverter;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import com.tradin.core.strategy.domain.vo.WinRate;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Rate {

    @Convert(converter = WinRateConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private WinRate winningRate;

    @Convert(converter = ProfitRateConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private ProfitRate simpleProfitRate; //단리 수익률

    @Convert(converter = ProfitRateConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private ProfitRate compoundProfitRate; // 복리 수익률

    @Convert(converter = ProfitRateConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private ProfitRate totalProfitRate; //총 수익률

    @Convert(converter = ProfitRateConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private ProfitRate totalLossRate; //총 손해율

    @Convert(converter = ProfitRateConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private ProfitRate averageProfitRate;

    @Builder
    private Rate(WinRate winningRate, ProfitRate simpleProfitRate, ProfitRate compoundProfitRate, ProfitRate totalProfitRate, ProfitRate totalLossRate, ProfitRate averageProfitRate) {
        this.winningRate = winningRate == null ? WinRate.of(null) : winningRate;
        this.simpleProfitRate = simpleProfitRate == null ? ProfitRate.of(null) : simpleProfitRate;
        this.compoundProfitRate = compoundProfitRate == null ? ProfitRate.of(null) : compoundProfitRate;
        this.totalProfitRate = totalProfitRate == null ? ProfitRate.of(null) : totalProfitRate;
        this.totalLossRate = totalLossRate == null ? ProfitRate.of(null) : totalLossRate;
        this.averageProfitRate = averageProfitRate == null ? ProfitRate.of(null) : averageProfitRate;
    }

    public static Rate of(WinRate winningRate, ProfitRate simpleProfitRate, ProfitRate compoundProfitRate, ProfitRate totalProfitRate, ProfitRate totalLossRate, ProfitRate averageProfitRate) {
        return Rate.builder()
            .winningRate(winningRate)
            .simpleProfitRate(simpleProfitRate)
            .compoundProfitRate(compoundProfitRate)
            .totalProfitRate(totalProfitRate)
            .totalLossRate(totalLossRate)
            .averageProfitRate(averageProfitRate)
            .build();
    }

    public void updateTotalProfitRate(ProfitRate profitRate) {
        this.totalProfitRate = this.totalProfitRate.add(profitRate);
    }

    public void updateTotalLossRate(ProfitRate lossRate) {
        this.totalLossRate = this.totalLossRate.subtract(lossRate);
    }

    public void updateWinRate(int winCount, int totalTradeCount) {
        if (totalTradeCount == 0) {
            this.winningRate = WinRate.of(null);
        } else {
            this.winningRate = WinRate.of(BigDecimal.valueOf(winCount).divide(BigDecimal.valueOf(totalTradeCount-1), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(100)));
        }
    }

    public void updateSimpleProfitRate() {
        this.simpleProfitRate = this.simpleProfitRate.add(this.simpleProfitRate);
    }

    public void updateCompoundProfitRate(ProfitRate profitRate) {
        this.compoundProfitRate = ProfitRate.of(
            (BigDecimal.ONE.add(this.compoundProfitRate.getValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                .multiply(BigDecimal.ONE.add(profitRate.getValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100))
        );
    }

    public void updateAverageProfitRate(int totalTradeCount) {
        if (totalTradeCount == 0) {
            this.averageProfitRate = ProfitRate.of(null);
        } else {
            this.averageProfitRate = ProfitRate.of(this.simpleProfitRate.getValue().divide(BigDecimal.valueOf(totalTradeCount), 2, RoundingMode.DOWN));
        }
    }

}
