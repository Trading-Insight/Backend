package com.tradin.module.futures.price.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class PriceCache {

    private final Map<String, BigDecimal> cache = new ConcurrentHashMap<>();

    public void updatePrice(String symbol, BigDecimal price) {
        cache.put(symbol.toUpperCase(), price);
    }

    public BigDecimal getPrice(String symbol) {
        return cache.get(symbol.toUpperCase());
    }

    public Map<String, BigDecimal> getAllPrices() {
        return cache;
    }
}
