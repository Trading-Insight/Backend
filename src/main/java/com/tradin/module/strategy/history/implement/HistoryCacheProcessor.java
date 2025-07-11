package com.tradin.module.strategy.history.implement;

import com.tradin.module.strategy.history.domain.repository.dao.HistoryDao;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryCacheProcessor {

    private final RedisTemplate<String, HistoryDao> historyRedisTemplate;

    @Async
    public void addHistoryCache(String cacheKey, List<HistoryDao> histories) {
        historyRedisTemplate.executePipelined(new SessionCallback<Void>() {
            @Override
            public Void execute(RedisOperations operations) {
                ZSetOperations<String, HistoryDao> zSetOperations = operations.opsForZSet();
                for (HistoryDao history : histories) {
                    double score = history.entryPosition().getTime().toInstant(ZoneOffset.UTC).toEpochMilli();
                    zSetOperations.add(cacheKey, history, score);
                }
                return null;
            }
        });
    }
}
