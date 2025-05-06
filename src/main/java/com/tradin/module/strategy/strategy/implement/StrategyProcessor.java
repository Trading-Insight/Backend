package com.tradin.module.strategy.strategy.implement;

import static com.tradin.module.strategy.strategy.domain.TimeFrameType.ONE_HOUR;

import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.strategy.domain.Count;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Rate;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.strategy.domain.StrategyType;
import com.tradin.module.strategy.strategy.domain.TradingType;
import com.tradin.module.strategy.strategy.domain.Type;
import com.tradin.module.strategy.strategy.domain.repository.StrategyRepository;
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
                0,
                0,
                0,
                0,
                0,
                0
            ),
            Count.of(
                0,
                0,
                0
            ),
            Position.of(
                TradingType.NONE,
                LocalDateTime.now(),
                0
            ),
            0,
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
