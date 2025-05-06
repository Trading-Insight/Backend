package com.tradin.module.futures.position.domain.repository;

import com.tradin.module.futures.position.domain.FuturesPosition;
import com.tradin.module.futures.position.domain.FuturesPositionQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FuturesPositionRepository extends JpaRepository<FuturesPosition, Long>, FuturesPositionQueryRepository {


}
