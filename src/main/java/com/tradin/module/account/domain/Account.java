package com.tradin.module.account.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tradin.common.jpa.AuditTime;
import com.tradin.module.balance.domain.Balance;
import com.tradin.module.users.domain.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
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

    private Boolean deletedYn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime deletedAt;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Balance> balances;


    @Builder
    public Account(String name, Users user) {
        this.name = name;
        this.user = user;
        this.deletedYn = false;
    }

    public void activateAutoTrade() {
        //this.autoTradeActivatedYn = true;
    }

    public void deactivateAutoTrade() {
        //this.autoTradeActivatedYn = false;
    }
}
