package com.tradin.core.futuresOrder.service.dto;

import com.tradin.core.futuresOrder.domain.repository.dao.FuturesOrderDao;
import java.util.List;

public record FuturesOrderResponseDto(List<FuturesOrderDao> futuresOrders) {
    public static FuturesOrderResponseDto of(List<FuturesOrderDao> futuresOrderDaos) {
        return new FuturesOrderResponseDto(futuresOrderDaos);
    }
}