package com.tradin.core.balance.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.repository.BalanceRepository;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.common.annotation.DistributedLock;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
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
        Balance balance = Balance.of(coinType, Amount.of(BigDecimal.ZERO), account);
        return balanceRepository.save(balance);
    }

    public Balance findByAccountIdAndCoinType(Long accountId, CoinType coinType) {
        return balanceRepository.findByAccountIdAndCoinType(accountId, coinType)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_BALANCE_EXCEPTION));
    }

    @DistributedLock(
        key = "'balance-update:' + #balance.id + ':' + #balance.coinType",
        waitTime = 5L,
        leaseTime = 10L,
        timeUnit = TimeUnit.SECONDS,
        fallbackMethod = "handleBalanceUpdateFallback"
    )
    @Transactional
    public void updateBalance(Balance balance, Amount amount) {
        balance.updateAmount(amount);
    }

    public Amount getUsdtAmount(Balance balance) {
        return balance.getAmount();
    }


    /**
     * 분산락 획득 실패 시 호출되는 fallback 메서드
     */
    public void handleBalanceUpdateFallback(Balance balance, Amount amount) {
        log.warn("분산락 획득 실패로 인한 잔고 업데이트 건너뜀 - accountId: {}, coinType: {}",
            balance.getAccount().getId(), balance.getCoinType());
    }
}
