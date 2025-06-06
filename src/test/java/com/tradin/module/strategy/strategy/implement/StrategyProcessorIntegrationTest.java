package com.tradin.module.strategy.strategy.implement;

import com.tradin.common.utils.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StrategyProcessorIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private StrategyProcessor strategyProcessor;

    @BeforeEach
    public void setUp() {

    }

    @Nested
    @DisplayName("수익이 난 경우")
    class Win {

        @Test
        public void 승률_정상_업데이트() throws Exception {

        }

        @Test
        public void 횟수_정상_업데이트() {

        }

        @Test
        public void 현재_포지션_정상_업데이트() {

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
