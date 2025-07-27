package com.tradin.core.fixture;


import com.tradin.core.history.domain.History;

public class HistoryFixture {

    /**
     * 기본 History 생성 (오픈 상태)
     */
    public static History createDefaultHistory() {
        return History.of(PositionFixture.createDefaultPosition(), StrategyFixture.createDefaultStrategy());
    }
}
