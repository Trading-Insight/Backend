package com.tradin.module.history.domain.repository;

import com.tradin.module.history.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>, HistoryQueryRepository {
}
