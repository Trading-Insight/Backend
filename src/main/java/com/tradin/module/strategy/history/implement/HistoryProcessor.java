package com.tradin.module.strategy.history.implement;

import com.tradin.module.strategy.history.domain.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryProcessor {

    private final HistoryRepository historyRepository;

}
