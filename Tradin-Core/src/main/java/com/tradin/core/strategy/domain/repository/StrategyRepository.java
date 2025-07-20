package com.tradin.core.strategy.domain.repository;


import com.tradin.core.strategy.domain.Strategy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long>, StrategyQueryRepository {
    Optional<Strategy> findByName(String name);
}
