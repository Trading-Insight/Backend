package com.tradin.module.subscription.domain;


import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.users.domain.Users;
import jakarta.persistence.Entity;
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
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "strategy_id", nullable = false)
    private Strategy strategy;

    private Boolean isActivated;

    @Builder
    public Subscription(Users user, Strategy strategy) {
        this.user = user;
        this.strategy = strategy;
        this.isActivated = true;
    }
}
