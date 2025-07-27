package com.tradin.core.price.domain;

import static com.tradin.core.common.exception.ExceptionType.NOT_FOUND_PRICE_EXCEPTION;

import com.tradin.core.common.exception.TradinException;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.price.domain.vo.Price;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class PriceCache {

    private final Map<CoinType, Price> cache = new ConcurrentHashMap<>();

    public void updatePrice(CoinType symbol, Price price) {
        cache.put(symbol, price);
    }

    public Price getPrice(CoinType symbol) {
        Price price = cache.get(symbol);
        if (price == null) {
            throw new TradinException(NOT_FOUND_PRICE_EXCEPTION, symbol);
        }
        return price;
    }

    public Map<CoinType, Price> getAllPrices() {
        return cache;
    }
}
