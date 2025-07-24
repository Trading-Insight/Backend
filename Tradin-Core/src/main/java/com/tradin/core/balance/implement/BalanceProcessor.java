package com.tradin.core.balance.implement;


import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.repository.BalanceRepository;
import com.tradin.core.balance.domain.vo.Money;
import com.tradin.core.strategy.domain.CoinType;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceProcessor {

    private final BalanceRepository balanceRepository;

    public Balance createUsdtBalance(Account account) {
        return createCoinBalance(account, CoinType.USDT);
    }

    public Balance createCoinBalance(Account account, CoinType coinType) {
        Balance balance = Balance.of(coinType, Money.of(BigDecimal.ZERO), account);
        return balanceRepository.save(balance);
    }

    public void updateBalance(Balance balance, Money amount) {
        balance.updateAmount(amount);
    }
}
