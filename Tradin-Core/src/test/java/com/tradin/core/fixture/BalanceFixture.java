package com.tradin.core.fixture;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.strategy.domain.CoinType;
import java.math.BigDecimal;

public class BalanceFixture {

    /**
     * 기본 Balance 생성 (USDT)
     */
    public static Balance createDefaultBalance() {
        return Balance.of(
            CoinType.USDT,
            Amount.of(new BigDecimal("10000.0000")),
            AccountFixture.createDefaultAccount()
        );
    }

    /**
     * 커스텀 Balance 생성
     */
    public static Balance createBalance(Account account, CoinType coinType, Amount amount) {
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
            .amount(Amount.of(new BigDecimal(amount)))
            .build();
    }

    /**
     * USDT Balance 생성
     */
    public static Balance createUsdtBalance(Account account, String amount) {
        return Balance.builder()
            .account(account)
            .coinType(CoinType.USDT)
            .amount(Amount.of(new BigDecimal(amount)))
            .build();
    }

    /**
     * ETH Balance 생성
     */
    public static Balance createEthBalance(Account account, String amount) {
        return Balance.builder()
            .account(account)
            .coinType(CoinType.ETH)
            .amount(Amount.of(new BigDecimal(amount)))
            .build();
    }

    /**
     * 잔액이 0인 Balance 생성
     */
    public static Balance createEmptyBalance(Account account, CoinType coinType) {
        return Balance.builder()
            .account(account)
            .coinType(coinType)
            .amount(Amount.of(BigDecimal.ZERO))
            .build();
    }
}
