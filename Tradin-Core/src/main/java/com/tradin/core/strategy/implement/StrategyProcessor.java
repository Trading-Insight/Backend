package com.tradin.core.strategy.implement;


import static com.tradin.core.strategy.domain.TimeFrameType.ONE_HOUR;

import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.Count;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Rate;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.StrategyType;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.strategy.domain.Type;
import com.tradin.core.strategy.domain.repository.StrategyRepository;
import java.math.BigDecimal;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import com.tradin.core.strategy.domain.vo.WinRate;
import com.tradin.core.strategy.domain.vo.ProfitFactor;
import com.tradin.core.price.domain.vo.Price;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyProcessor {

    private final StrategyRepository strategyRepository;

    public void createStrategy() {
        Strategy strategy = Strategy.of(
            "test",
            Type.of(StrategyType.FUTURE, CoinType.BTC, ONE_HOUR),
            Rate.of(
                WinRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO)
            ),
            Count.of(
                0,
                0,
                0
            ),
            Position.of(
                TradingType.NONE,
                LocalDateTime.now(),
                Price.of(BigDecimal.ZERO)
            ),
            ProfitFactor.of(BigDecimal.ZERO),
            0
        );
        strategyRepository.save(strategy);
    }

    public void updateRateAndCount(Strategy strategy, Position position) {
        strategy.updateRateAndCount(position.getPrice(), position.getTime());
    }

    public void updateCurrentPosition(Strategy strategy, Position position) {
        strategy.updateCurrentPosition(position);
    }


}
