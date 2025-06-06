package com.tradin.common.fixture;

import com.tradin.module.futures.position.fixture.PositionFixture;
import com.tradin.module.strategy.history.domain.History;

public class HistoryFixture {

    /**
     * 기본 History 생성 (오픈 상태)
     */
    public static History createDefaultHistory() {
        return History.of(PositionFixture.createDefaultPosition(), StrategyFixture.createDefaultStrategy());
    }
}
