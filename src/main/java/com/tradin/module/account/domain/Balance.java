package com.tradin.module.account.domain;

import com.tradin.module.strategy.domain.CoinType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Balance {

    private CoinType coinType;
    private Double amount;
}
