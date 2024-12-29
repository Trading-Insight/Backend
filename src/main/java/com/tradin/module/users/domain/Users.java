package com.tradin.module.users.domain;

import com.tradin.common.jpa.AuditTime;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.strategy.domain.TradingType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends AuditTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sub;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int leverage;

    @Column(nullable = false)
    private int quantityRate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType tradingType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType currentPositionType;

    @Embedded
    private SocialInfo socialInfo;

    @Column(unique = true)
    private String binanceApiKey;

    @Column(unique = true)
    private String binanceSecretKey;

    @JoinColumn(name = "strategy_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Strategy strategy;

    @Builder
    private Users(String sub, String email, String socialId, UserSocialType socialType) {
        this.name = "트레이더";
        this.sub = sub;
        this.email = email;
        this.socialInfo = SocialInfo.of(socialId, socialType);
        this.leverage = 1;
        this.quantityRate = 100;
        this.tradingType = TradingType.BOTH;
        this.currentPositionType = TradingType.NONE;
        this.binanceApiKey = null;
        this.binanceSecretKey = null;
        this.strategy = null;
    }

    public void subscribeStrategy(Strategy strategy, String encryptedApiKey, String encryptedSecretKey, int leverage, int quantityRate, TradingType tradingType) {
        this.strategy = strategy;
        this.binanceApiKey = encryptedApiKey;
        this.binanceSecretKey = encryptedSecretKey;
        this.leverage = leverage;
        this.quantityRate = quantityRate;
        this.tradingType = tradingType;
    }

    public void changeLeverage(int leverage) {
        this.leverage = leverage;
    }

    public void changeQuantityRate(int quantityRate) {
        this.quantityRate = quantityRate;
    }

    public void changeTradingType(TradingType tradingType) {
        this.tradingType = tradingType;
    }

    public void unsubscribeStrategy() {
        this.strategy = null;
    }

    public void changeCurrentPosition(TradingType tradingType) {
        this.currentPositionType = tradingType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.id.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
