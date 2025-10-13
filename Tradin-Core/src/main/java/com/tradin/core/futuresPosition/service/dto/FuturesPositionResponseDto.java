package com.tradin.core.futuresPosition.service.dto;

import com.tradin.core.futuresPosition.domain.repository.dao.FuturesPositionDao;
import java.util.List;

public record FuturesPositionResponseDto(List<FuturesPositionDao> futuresPositions) {
    public static FuturesPositionResponseDto of(List<FuturesPositionDao> futuresPositionDaos) {
        return new FuturesPositionResponseDto(futuresPositionDaos);
    }

}
