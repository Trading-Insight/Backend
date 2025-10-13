package com.tradin.core.futuresOrder.domain.repository.impl;

import static com.tradin.core.futuresOrder.domain.QFuturesOrder.futuresOrder;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.core.futuresOrder.domain.repository.FuturesOrderQueryRepository;
import com.tradin.core.futuresOrder.domain.repository.dao.FuturesOrderDao;
import com.tradin.core.futuresOrder.domain.repository.dao.QFuturesOrderDao;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FuturesOrderQueryRepositoryImpl implements FuturesOrderQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<FuturesOrderDao> findFuturesOrderDaosByAccountId(Long accountId) {
        return jpaQueryFactory.select(new QFuturesOrderDao(futuresOrder.id, futuresOrder.tradingType, futuresOrder.coinType, futuresOrder.price, futuresOrder.amount, futuresOrder.margin, futuresOrder.orderStatus, futuresOrder.createdAt, futuresOrder.updatedAt))
            .from(futuresOrder)
            .where(futuresOrder.account.id.eq(accountId))
            .orderBy(futuresOrder.id.desc())
            .fetch();
    }
}
