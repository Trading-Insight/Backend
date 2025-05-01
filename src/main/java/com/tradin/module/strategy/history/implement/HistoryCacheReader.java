package com.tradin.module.strategy.history.implement;

import com.tradin.module.strategy.history.domain.repository.dao.HistoryDao;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryCacheReader {

    private final RedisTemplate<String, HistoryDao> historyRedisTemplate;

    public List<HistoryDao> readHistoryByIdAndPeriod(String cacheKey, LocalDate startDate, LocalDate endDate, Pageable pageable) {

        ZSetOperations<String, HistoryDao> historyCaches = historyRedisTemplate.opsForZSet();

        Set<HistoryDao> historySet = historyCaches.rangeByScore(
            cacheKey,
            convertLocalDateToEpochSecond(startDate),
            convertLocalDateToEpochSecond(endDate),
            pageable.getOffset(),
            pageable.getPageSize()
        );

        return new ArrayList<>(historySet);
    }

    private long convertLocalDateToEpochSecond(LocalDate date) {
        return date.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
    }
}
