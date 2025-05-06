package com.tradin.module.strategy.subscription.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.users.account.domain.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "strategy_id"}))
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "strategy_id", nullable = false)
    private Strategy strategy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime endDate;

    @Builder
    public Subscription(Account account, Strategy strategy) {
        this.account = account;
        this.strategy = strategy;
        this.status = SubscriptionStatus.ACTIVE;
        this.startDate = LocalDateTime.now();
    }

    public static Subscription of(Account account, Strategy strategy) {
        return Subscription.builder()
            .account(account)
            .strategy(strategy)
            .build();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Boolean isActivated() {
        return status.isActivated();
    }


    public Boolean isDeActivated() {
        return status.isDeActivated();
    }

    public void activate() {
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void deActivate() {
        this.status = SubscriptionStatus.INACTIVE;
        this.endDate = LocalDateTime.now();
    }
}
