package com.tradin.core.balance.domain.repository;


import com.tradin.core.balance.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long>, BalanceQueryRepository {


}
