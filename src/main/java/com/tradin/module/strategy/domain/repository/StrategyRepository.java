package com.tradin.module.strategy.domain.repository;

import com.tradin.module.strategy.domain.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long>, StrategyQueryRepository {
    Optional<Strategy> findByName(String name);
}
