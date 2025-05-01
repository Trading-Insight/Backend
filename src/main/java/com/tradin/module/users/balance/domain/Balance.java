package com.tradin.module.users.balance.domain;

import com.tradin.module.users.account.domain.Account;
import com.tradin.module.strategy.strategy.domain.CoinType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CoinType coinType;

    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public Balance(CoinType coinType, Double amount, Account account) {
        this.coinType = coinType;
        this.amount = amount;
        this.account = account;
    }

    public static Balance of(CoinType coinType, Double amount, Account account) {
        return Balance.builder()
            .coinType(coinType)
            .amount(amount)
            .account(account)
            .build();
    }

}
