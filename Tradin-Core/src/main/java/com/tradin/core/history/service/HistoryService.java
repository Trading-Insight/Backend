package com.tradin.core.history.service;

import static com.tradin.core.common.exception.ExceptionType.NOT_FOUND_HISTORY_EXCEPTION;

import com.tradin.core.common.exception.TradinException;
import com.tradin.core.history.domain.History;
import com.tradin.core.history.domain.repository.HistoryRepository;
import com.tradin.core.history.domain.repository.dao.HistoryDao;
import com.tradin.core.history.service.dto.BackTestDto;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.TradingType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import com.tradin.core.price.domain.vo.Price;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final String cacheKeyPrefix = "strategyId:";

    private final HistoryRepository historyRepository;
    private final RedisTemplate<String, HistoryDao> historyRedisTemplate;

    public void createTestHistory(Strategy strategy) {
        Position position = Position.of(TradingType.SHORT, LocalDateTime.now(), Price.of(new BigDecimal(90000)));
        createHistoryEntity(strategy, position);
    }

    public void closeAndCreateHistory(Strategy strategy, Position position) {
        closeOpenHistory(strategy.getId(), position);
        createHistoryEntity(strategy, position);
    }


    public List<HistoryDao> backTest(BackTestDto request, Pageable pageable) {
        return findAllAndFilterByStrategyIdAndPeriodAndTradingType(request.getId(), request.getStartDate(), request.getEndDate(), request.getTradingType(), pageable);

    }

    private List<HistoryDao> findAllAndFilterByStrategyIdAndPeriodAndTradingType(Long strategyId, LocalDate startDate, LocalDate endDate, TradingType tradingType, Pageable pageable) {
        List<HistoryDao> historyDaos = findAllByStrategyIdAndPeriodInCache(strategyId, startDate, endDate, pageable);

        if (historyDaos.isEmpty()) {
            historyDaos = historyRepository.findHistoryDaosByStrategyId(strategyId);
            addHistoryDaosToCache(strategyId, historyDaos);
        }

        return historyDaos.stream()
            .filter(history -> isInPeriod(history, startDate, endDate))
            .filter(history -> isCorrespondTradingType(history, tradingType))
            .toList();
    }


    private void createHistoryEntity(Strategy strategy, Position position) {
        History newHistory = History.of(position, strategy);
        historyRepository.save(newHistory);
    }

    private void closeOpenHistory(Long strategyId, Position position) {
        History history = findOpenHistoryByStrategyId(strategyId);
        history.closeHistory(position);
    }

    private boolean isInPeriod(HistoryDao history, LocalDate startDate, LocalDate endDate) {
        return history.entryPosition().getTime().toLocalDate().isAfter(startDate) &&
            history.exitPosition().getTime().toLocalDate().isBefore(endDate);
    }

    private boolean isCorrespondTradingType(HistoryDao history, TradingType tradingType) {
        return switch (tradingType) {
            case LONG -> history.entryPosition().getTradingType().equals(TradingType.LONG);
            case SHORT -> history.entryPosition().getTradingType().equals(TradingType.SHORT);
            case BOTH -> history.entryPosition().getTradingType().equals(TradingType.LONG) ||
                history.entryPosition().getTradingType().equals(TradingType.SHORT);
            default -> false;
        };
    }

    private History findOpenHistoryByStrategyId(Long strategyId) {
        return historyRepository.findOpenHistoryByStrategyId(strategyId)
            .orElseThrow(() -> new TradinException(NOT_FOUND_HISTORY_EXCEPTION));
    }

    private List<HistoryDao> findAllByStrategyIdAndPeriodInCache(Long strategyId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        final String cacheKey = cacheKeyPrefix + strategyId;

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

    public void addHistoryDaosToCache(Long strategyId, List<HistoryDao> histories) {
        final String cacheKey = cacheKeyPrefix + strategyId;

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
