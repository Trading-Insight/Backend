package com.tradin.core.balance.domain;

import com.tradin.core.common.jpa.AuditTime;
import com.tradin.core.account.domain.Account;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.balance.domain.vo.Money;
import com.tradin.core.common.converter.MoneyConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "coin_type"}))
public class Balance extends AuditTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Convert(converter = MoneyConverter.class)
    @Column(nullable = false, precision = 20, scale = 4)
    private Money amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public Balance(CoinType coinType, Money amount, Account account) {

        this.coinType = coinType;
        this.amount = amount;
        this.account = account;
    }

    public static Balance of(CoinType coinType, Money amount, Account account) {
        return Balance.builder()
            .coinType(coinType)
            .amount(amount)
            .account(account)
            .build();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void updateAmount(Money amount) {
        this.amount = this.amount.add(amount);
    }
}
