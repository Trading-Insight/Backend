package com.tradin.module.users.balance.domain;

import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.users.account.domain.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "coin_type"}))
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public Balance(CoinType coinType, BigDecimal amount, Account account) {
        this.coinType = coinType;
        this.amount = amount;
        this.account = account;
    }

    public static Balance of(CoinType coinType, BigDecimal amount, Account account) {
        return Balance.builder()
            .coinType(coinType)
            .amount(amount)
            .account(account)
            .build();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void updateAmount(BigDecimal amount) {
        this.amount = this.amount.add(amount).setScale(2, RoundingMode.DOWN);
    }
}
