package com.tradin.module.futures.position.implement;

import com.tradin.module.futures.position.domain.FuturesPosition;
import com.tradin.module.futures.position.domain.repository.FuturesPositionRepository;
import com.tradin.module.strategy.strategy.domain.CoinType;
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
