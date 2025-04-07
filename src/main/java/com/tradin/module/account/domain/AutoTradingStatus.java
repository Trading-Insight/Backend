package com.tradin.module.account.domain;

import com.tradin.module.strategy.domain.CoinType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutoTradingStatus {

    private CoinType coinType;
    private Boolean autoTradeActivatedYn;
}
