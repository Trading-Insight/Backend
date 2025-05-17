package com.tradin.module.users.balance.domain.repository;

import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.users.balance.domain.Balance;
import java.util.Optional;

public interface BalanceQueryRepository {

    Optional<Balance> findByAccountIdAndCoinType(Long accountId, CoinType coinType);
}
