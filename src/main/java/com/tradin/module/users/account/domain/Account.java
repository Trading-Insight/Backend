package com.tradin.module.users.account.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tradin.common.jpa.AuditTime;
import com.tradin.module.futures.position.domain.FuturesPosition;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.subscription.domain.Subscription;
import com.tradin.module.users.balance.domain.Balance;
import com.tradin.module.users.users.domain.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends AuditTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isDeleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private final List<Balance> balances = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private final List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private final List<FuturesPosition> futuresPositions = new ArrayList<>();

    @Builder
    public Account(Users user) {
        this.user = user;
        this.name = "유저";
        this.isDeleted = false;
    }

    public static Account of(Users user) {
        return Account.builder()
            .user(user)
            .build();
    }

    public void addBalance(Balance balance) {
        this.balances.add(balance);
        balance.setAccount(this);
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
        subscription.setAccount(this);
    }

    public void removeSubscription(Subscription subscription) {
        this.subscriptions.remove(subscription);
        subscription.setAccount(null);
    }

    public void addFuturesPosition(FuturesPosition futuresPosition) {
        this.futuresPositions.add(futuresPosition);
        futuresPosition.setAccount(this);
    }

    public void removeFuturesPosition(FuturesPosition futuresPosition) {
        this.futuresPositions.remove(futuresPosition);
        futuresPosition.setAccount(null);
    }

    public Optional<Balance> getBalanceByCoinType(CoinType coinType) {
        return this.balances.stream()
            .filter(balance -> balance.getCoinType().isMatch(coinType))
            .findAny();
    }

    public Optional<FuturesPosition> getOpenFuturesPositionByCoinType(CoinType coinType) {
        return this.futuresPositions.stream()
            .filter(futuresPosition -> futuresPosition.getCoinType().isMatch(coinType) && futuresPosition.getAmount()
                .compareTo(BigDecimal.ZERO) > 0)
            .findAny();
    }

}
