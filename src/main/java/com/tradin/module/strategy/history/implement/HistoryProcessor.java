package com.tradin.module.strategy.history.implement;

import com.tradin.module.strategy.history.domain.History;
import com.tradin.module.strategy.history.domain.repository.HistoryRepository;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryProcessor {

    private final HistoryRepository historyRepository;

    public void createHistory(Strategy strategy, Position position) {
        History newHistory = History.of(position, strategy);
        historyRepository.save(newHistory);
    }

    public void closeHistory(History history, Position position) {
        history.closeHistory(position);
    }
}
