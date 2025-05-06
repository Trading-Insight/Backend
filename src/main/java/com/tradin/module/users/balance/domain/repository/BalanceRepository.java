package com.tradin.module.users.balance.domain.repository;

import com.tradin.module.users.balance.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long>, BalanceQueryRepository {


}
