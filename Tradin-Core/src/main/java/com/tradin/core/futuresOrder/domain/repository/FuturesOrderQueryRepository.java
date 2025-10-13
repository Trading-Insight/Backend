package com.tradin.core.futuresOrder.domain.repository;

import com.tradin.core.futuresOrder.domain.repository.dao.FuturesOrderDao;
import java.util.List;
import java.util.Optional;

public interface FuturesOrderQueryRepository {
    List<FuturesOrderDao> findFuturesOrderDaosByAccountId(Long accountId);
}
