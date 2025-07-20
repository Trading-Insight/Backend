package com.tradin.core.futuresPosition.implement;

import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.futuresPosition.domain.repository.FuturesPositionRepository;
import com.tradin.core.strategy.domain.CoinType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FuturesPositionReader {

    private final FuturesPositionRepository futuresPositionRepository;

    public Optional<FuturesPosition> findOpenFuturesPositionByAccountAndCoinType(Long accountId, CoinType coinType) {
        return futuresPositionRepository.findOpenFuturesPositionByAccountAndCoinType(accountId, coinType);
    }
}
