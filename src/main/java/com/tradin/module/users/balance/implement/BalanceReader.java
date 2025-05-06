package com.tradin.module.users.balance.implement;

import com.tradin.common.exception.ExceptionType;
import com.tradin.common.exception.TradinException;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.users.balance.domain.Balance;
import com.tradin.module.users.balance.domain.repository.BalanceRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceReader {

    private final BalanceRepository balanceRepository;

    public Balance findByAccountIdAndCoinTypeForUpdate(Long accountId, CoinType coinType) {
        return balanceRepository.findByAccountIdAndCoinTypeForUpdate(accountId, coinType)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_BALANCE_EXCEPTION));
    }

    public BigDecimal getUsdtAmount(Balance balance) {
        return balance.getAmount();
    }

}
