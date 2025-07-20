package com.tradin.core.balance.implement;

import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;

import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.repository.BalanceRepository;
import com.tradin.core.strategy.domain.CoinType;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceReader {

    private final BalanceRepository balanceRepository;

    public Balance findByAccountIdAndCoinType(Long accountId, CoinType coinType) {
        return balanceRepository.findByAccountIdAndCoinType(accountId, coinType)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_BALANCE_EXCEPTION));
    }

    public BigDecimal getUsdtAmount(Balance balance) {
        return balance.getAmount();
    }

}
