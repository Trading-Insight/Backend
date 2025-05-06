package com.tradin.module.futures.price.domain;

import static com.tradin.common.exception.ExceptionType.NOT_FOUND_PRICE_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.strategy.strategy.domain.CoinType;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class PriceCache {

    private final Map<CoinType, BigDecimal> cache = new ConcurrentHashMap<>();

    public void updatePrice(CoinType symbol, BigDecimal price) {
        cache.put(symbol, price);
    }

    public BigDecimal getPrice(CoinType symbol) {
        BigDecimal price = cache.get(symbol);
        if (price == null) {
            throw new TradinException(NOT_FOUND_PRICE_EXCEPTION, symbol);
        }
        return price;
    }

    public Map<CoinType, BigDecimal> getAllPrices() {
        return cache;
    }
}
