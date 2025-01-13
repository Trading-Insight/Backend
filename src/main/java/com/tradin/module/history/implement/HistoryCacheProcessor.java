package com.tradin.module.history.implement;

import com.tradin.module.history.domain.repository.dao.HistoryDao;
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
        historyRedisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) {
                ZSetOperations<K, V> ops = operations.opsForZSet();
                for (HistoryDao history : histories) {
                    ops.add((K) cacheKey, (V) history, history.entryPosition().getTime().toEpochSecond(ZoneOffset.UTC));
                }
                return null;
            }
        });
    }
}
