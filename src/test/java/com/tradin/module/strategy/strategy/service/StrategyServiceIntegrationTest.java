package com.tradin.module.strategy.strategy.service;

import com.tradin.common.utils.BaseIntegrationTest;
import com.tradin.module.strategy.strategy.implement.StrategyProcessor;
import com.tradin.module.strategy.strategy.implement.StrategyReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StrategyServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private StrategyReader strategyReader;

    @Autowired
    private StrategyProcessor strategyProcessor;

    @Nested
    @DisplayName("수익이 난 경우")
    class win {

        @Test
        void 전략_정상_업데이트() throws Exception {

        }
    }


    @Nested
    @DisplayName("수익이 나지 않은 경우")
    class Loss {

        @Test
        void 전략_정상_업데이트() throws Exception {

        }
    }
}
