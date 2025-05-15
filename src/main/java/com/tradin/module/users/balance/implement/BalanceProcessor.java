package com.tradin.module.users.balance.implement;

import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.balance.domain.Balance;
import com.tradin.module.users.balance.domain.repository.BalanceRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceProcessor {

    private final BalanceRepository balanceRepository;

    public Balance createUsdtBalance(Account account) {
        return Balance.of(CoinType.USDT, BigDecimal.valueOf(10000), account);
    }

    public Balance createCoinBalance(Account account, CoinType coinType) {
        Balance balance = Balance.of(coinType, BigDecimal.ZERO, account);
        return balanceRepository.save(balance);
    }

    public void updateBalance(Balance balance, BigDecimal amount) {
        balance.updateAmount(amount);
    }
}
