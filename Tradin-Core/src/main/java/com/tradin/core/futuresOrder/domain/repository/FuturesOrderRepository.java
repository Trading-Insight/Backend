package com.tradin.core.futuresOrder.domain.repository;

import com.tradin.core.futuresOrder.domain.FuturesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuturesOrderRepository extends JpaRepository<FuturesOrder, Long>, FuturesOrderQueryRepository {

}
