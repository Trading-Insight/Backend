package com.tradin.core.balance.service.dto;

import com.tradin.core.balance.domain.repository.dao.BalanceDaos;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.strategy.domain.CoinType;
import java.util.List;

public record BalanceResponseDto(
    List<BalanceDaos> balances
) {
    public static BalanceResponseDto of(List<BalanceDaos> balanceDaos) {
        return new BalanceResponseDto(balanceDaos);
    }
}
