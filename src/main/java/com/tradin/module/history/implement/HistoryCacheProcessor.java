package com.tradin.module.history.implement;

import com.tradin.module.history.domain.repository.dao.HistoryDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryCacheProcessor {

    public void addHistoryCache(String cacheKey, List<HistoryDao> historyDaos) {
        //TODO - 캐시 추가 로직 event
    }
}
