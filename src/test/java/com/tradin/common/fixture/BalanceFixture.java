package com.tradin.common.fixture;

import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.balance.domain.Balance;
import java.math.BigDecimal;

public class BalanceFixture {

    /**
     * 기본 Balance 생성 (USDT)
     */
    public static Balance createDefaultBalance() {
        return Balance.of(
            CoinType.USDT,
            new BigDecimal("10000.0000"),
            AccountFixture.createDefaultAccount()
        );
    }

    /**
     * 커스텀 Balance 생성
     */
    public static Balance createBalance(Account account, CoinType coinType, BigDecimal amount) {
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
            .amount(new BigDecimal(amount))
            .build();
    }

    /**
     * USDT Balance 생성
     */
    public static Balance createUsdtBalance(Account account, String amount) {
        return Balance.builder()
            .account(account)
            .coinType(CoinType.USDT)
            .amount(new BigDecimal(amount))
            .build();
    }

    /**
     * ETH Balance 생성
     */
    public static Balance createEthBalance(Account account, String amount) {
        return Balance.builder()
            .account(account)
            .coinType(CoinType.ETH)
            .amount(new BigDecimal(amount))
            .build();
    }

    /**
     * 잔액이 0인 Balance 생성
     */
    public static Balance createEmptyBalance(Account account, CoinType coinType) {
        return Balance.builder()
            .account(account)
            .coinType(coinType)
            .amount(BigDecimal.ZERO)
            .build();
    }
}
