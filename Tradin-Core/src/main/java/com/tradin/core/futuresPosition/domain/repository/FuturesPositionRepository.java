package com.tradin.core.futuresPosition.domain.repository;


import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.futuresPosition.domain.FuturesPositionQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FuturesPositionRepository extends JpaRepository<FuturesPosition, Long>, FuturesPositionQueryRepository {


}
