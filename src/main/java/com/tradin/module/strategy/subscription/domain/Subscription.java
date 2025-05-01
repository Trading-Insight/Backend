package com.tradin.module.strategy.subscription.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.users.account.domain.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    }
}
