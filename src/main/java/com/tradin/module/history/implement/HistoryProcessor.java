package com.tradin.module.history.implement;

import com.tradin.module.history.domain.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryProcessor {

    private final HistoryRepository historyRepository;

}
