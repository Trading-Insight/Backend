package com.tradin.core.balance.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.strategy.domain.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceFacadeService {
    private final BalanceService balanceService;

    @Transactional
    public Balance createUsdtBalance(Account account) {
        return balanceService.createUsdtBalance(account);
    }

    @Transactional
    public Balance createCoinBalance(Account account, CoinType coinType) {
        return balanceService.createCoinBalance(account, coinType);
    }

    @Transactional(readOnly = true)
    public Balance findByAccountIdAndCoinType(Long accountId, CoinType coinType) {
        return balanceService.findByAccountIdAndCoinType(accountId, coinType);
    }

    @Transactional
    public void updateBalance(Long accountId, CoinType coinType, BigDecimal amount) {
        balanceService.updateBalance(accountId, coinType, amount);
    }

    @Transactional(readOnly = true)
    public Amount getUsdtAmount(Balance balance) {
        return balanceService.getUsdtAmount(balance);
    }
}
