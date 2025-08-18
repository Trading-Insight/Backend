package com.tradin.core.balance.domain;

import static com.tradin.core.common.exception.ExceptionType.INSUFFICIENT_BALANCE_EXCEPTION;
import static com.tradin.core.common.exception.ExceptionType.INVALID_AMOUNT_EXCEPTION;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.common.converter.AmountConverter;
import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.common.jpa.AuditTime;
import com.tradin.core.strategy.domain.CoinType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "balance", indexes = {
    @Index(name = "idx_balance_account_id", columnList = "account_id"),
    @Index(name = "idx_balance_coin_type", columnList = "coin_type"),
    @Index(name = "idx_balance_account_coin", columnList = "account_id, coin_type")
})
public class Balance extends AuditTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Convert(converter = AmountConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private Amount amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder
    public Balance(CoinType coinType, Amount amount, Account account) {
        this.coinType = coinType;
        this.amount = amount;
        this.account = account;
    }

    public static Balance of(CoinType coinType, Amount amount, Account account) {
        return Balance.builder()
            .coinType(coinType)
            .amount(amount)
            .account(account)
            .build();
    }

    public void updateAmount(Amount amount) {
        this.amount = amount;
    }

    public void subtractMargin(Amount amount) {
        if (amount.isNegative()) {
            throw new TradinException(INVALID_AMOUNT_EXCEPTION);
        }

        if (this.amount.isLessThan(amount)) {
            throw new TradinException(INSUFFICIENT_BALANCE_EXCEPTION);
        }
        this.amount = this.amount.subtract(amount);
    }

    public void addBalance(Amount amount) {
        if (amount.isNegative()) {
            throw new TradinException(INVALID_AMOUNT_EXCEPTION);
        }
        this.amount = this.amount.add(amount);
    }

    public void settleProfit(Amount margin, Amount profitAmount) {
        addBalance(margin);

        if (profitAmount.isNegative()) {
            subtractMargin(profitAmount.negate());
        }
        addBalance(profitAmount);
    }

    public Amount getAmount() {
        return this.amount;
    }
}
