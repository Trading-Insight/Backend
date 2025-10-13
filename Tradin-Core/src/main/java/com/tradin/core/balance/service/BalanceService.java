package com.tradin.core.balance.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.repository.BalanceRepository;
import com.tradin.core.balance.domain.repository.dao.BalanceDaos;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.common.annotation.DistributedLock;
import com.tradin.core.futuresOrder.domain.vo.Margin;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public Balance createUsdtBalance(Account account) {
        return createCoinBalance(account, CoinType.USDT);
    }

    public Balance createCoinBalance(Account account, CoinType coinType) {
        Balance balance = Balance.of(coinType, Amount.of(BigDecimal.valueOf(10000)), account);
        return balanceRepository.save(balance);
    }

    public Balance findByAccountIdAndCoinType(Long accountId, CoinType coinType) {
        return balanceRepository.findByAccountIdAndCoinType(accountId, coinType)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_BALANCE_EXCEPTION));
    }

    public Balance findUsdtBalanceByAccount(Long accountId) {
        return balanceRepository.findByAccountIdAndCoinType(accountId, CoinType.USDT)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_BALANCE_EXCEPTION));
    }

    public void subtractMargin(Balance balance, Amount amount) {
        balance.subtractMargin(amount);
    }

    public void settleProfit(Balance balance, Margin margin, Amount profitAmount) {
        balance.settleProfit(Amount.of(margin.getValue()), profitAmount);
    }

    public void updateBalance(Balance balance, Amount amount) {
        balance.updateAmount(amount);
    }

    public Amount getUsdtAmount(Balance balance) {
        return balance.getAmount();
    }

    public List<BalanceDaos> findBalanceDaosByAccountId(Long accountId) {
        return balanceRepository.findBalanceDaosByAccountId(accountId);
    }
}
