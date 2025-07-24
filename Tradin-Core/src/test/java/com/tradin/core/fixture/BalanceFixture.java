package com.tradin.core.fixture;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.vo.Money;
import com.tradin.core.strategy.domain.CoinType;
import java.math.BigDecimal;

public class BalanceFixture {

    /**
     * 기본 Balance 생성 (USDT)
     */
    public static Balance createDefaultBalance() {
        return Balance.of(
            CoinType.USDT,
            Money.of(new BigDecimal("10000.0000")),
            AccountFixture.createDefaultAccount()
        );
    }

    /**
     * 커스텀 Balance 생성
     */
    public static Balance createBalance(Account account, CoinType coinType, Money amount) {
        return Balance.builder()
            .account(account)
            .coinType(coinType)
            .amount(amount)
            .build();
    }

    /**
     * BTC Balance 생성
     */
    public static Balance createBtcBalance(Account account, String amount) {
        return Balance.builder()
            .account(account)
            .coinType(CoinType.BTC)
            .amount(Money.of(new BigDecimal(amount)))
            .build();
    }

    /**
     * USDT Balance 생성
     */
    public static Balance createUsdtBalance(Account account, String amount) {
        return Balance.builder()
            .account(account)
            .coinType(CoinType.USDT)
            .amount(Money.of(new BigDecimal(amount)))
            .build();
    }

    /**
     * ETH Balance 생성
     */
    public static Balance createEthBalance(Account account, String amount) {
        return Balance.builder()
            .account(account)
            .coinType(CoinType.ETH)
            .amount(Money.of(new BigDecimal(amount)))
            .build();
    }

    /**
     * 잔액이 0인 Balance 생성
     */
    public static Balance createEmptyBalance(Account account, CoinType coinType) {
        return Balance.builder()
            .account(account)
            .coinType(coinType)
            .amount(Money.of(BigDecimal.ZERO))
            .build();
    }
}
