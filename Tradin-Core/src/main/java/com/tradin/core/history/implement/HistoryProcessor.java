package com.tradin.core.history.implement;


import com.tradin.core.history.domain.History;
import com.tradin.core.history.domain.repository.HistoryRepository;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
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
