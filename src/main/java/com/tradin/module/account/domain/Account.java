package com.tradin.module.account.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tradin.common.jpa.AuditTime;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.users.domain.Users;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends AuditTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Balance balance;

    private Boolean autoTradeActivatedYn;

    private Boolean deletedYn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime deletedAt;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @JoinColumn(name = "strategy_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Strategy strategy;

    @Builder
    public Account(String name, Users user) {
        this.name = name;
        this.autoTradeActivatedYn = false;
        this.user = user;
        this.deletedYn = false;
    }

    public void activateAutoTrade() {
        this.autoTradeActivatedYn = true;
    }

    public void deactivateAutoTrade() {
        this.autoTradeActivatedYn = false;
    }
}
