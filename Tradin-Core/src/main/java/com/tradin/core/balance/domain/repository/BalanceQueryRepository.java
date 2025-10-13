package com.tradin.core.balance.domain.repository;

import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.repository.dao.BalanceDaos;
import com.tradin.core.strategy.domain.CoinType;
import java.util.List;
import java.util.Optional;

public interface BalanceQueryRepository {

    Optional<Balance> findByAccountIdAndCoinType(Long accountId, CoinType coinType);
    List<BalanceDaos> findBalanceDaosByAccountId(Long accountId);
}
