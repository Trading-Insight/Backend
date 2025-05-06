package com.tradin.module.futures.order.domain.repository;

import com.tradin.module.futures.order.domain.FuturesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuturesOrderRepository extends JpaRepository<FuturesOrder, Long> {

}
