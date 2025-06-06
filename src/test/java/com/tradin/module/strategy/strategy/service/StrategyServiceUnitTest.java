package com.tradin.module.strategy.strategy.service;

import static com.tradin.common.exception.ExceptionType.NOT_FOUND_HISTORY_EXCEPTION;
import static com.tradin.common.exception.ExceptionType.SAME_POSITION_REQUEST_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.tradin.common.exception.ExceptionType;
import com.tradin.common.exception.TradinException;
import com.tradin.common.fixture.StrategyFixture;
import com.tradin.common.utils.BaseServiceUnitTest;
import com.tradin.module.futures.position.fixture.PositionFixture;
import com.tradin.module.strategy.history.implement.HistoryProcessor;
import com.tradin.module.strategy.history.implement.HistoryReader;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.strategy.implement.StrategyProcessor;
import com.tradin.module.strategy.strategy.implement.StrategyReader;
import com.tradin.module.strategy.strategy.service.dto.WebHookDto;
import com.tradin.module.strategy.subscription.implement.SubscriptionReader;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class StrategyServiceUnitTest extends BaseServiceUnitTest {


    @InjectMocks
    private StrategyService strategyService;

    @Mock
    private StrategyReader strategyReader;

    @Mock
    private HistoryReader historyReader;

    @Mock
    private SubscriptionReader subscriptionReader;

    @Mock
    private StrategyProcessor strategyProcessor;

    @Mock
    private HistoryProcessor historyProcessor;

    @Test
    void 웹훅_처리_정상_동작() throws Exception {
        //given
        Strategy strategy = StrategyFixture.get();
        Position position = PositionFixture.get();

        //when & then
        assertDoesNotThrow(() -> strategyService.handleFutureWebHook(WebHookDto.of(strategy.getId(), position)));
    }

    @Test
    void 웹훅_처리시_존재하지_않는_전략이면_예외_발생() throws Exception {
        //given
        Position position = PositionFixture.get();

        when(strategyReader.findStrategyById(anyLong()))
            .thenThrow(new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));

        //when & then
        assertThatThrownBy(() ->
            strategyService.handleFutureWebHook(WebHookDto.of(anyLong(), position)))
            .isInstanceOf(TradinException.class)
            .satisfies(e -> {
                TradinException te = (TradinException) e;
                assertThat(te.getErrorType()).isEqualTo(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION);
            });
    }

    @Test
    void 웹훅_처리시_동일_포지션_요청이_오면_예외_발생() throws Exception {
        // given
        long strategyId = 1L;
        Position position = PositionFixture.get();
        Strategy strategy = StrategyFixture.get();
        strategy.updateCurrentPosition(position);

        when(strategyReader.findStrategyById(strategyId))
            .thenReturn(strategy);

        WebHookDto dto = WebHookDto.of(strategyId, position);

        // when & then
        assertThatThrownBy(() -> strategyService.handleFutureWebHook(dto))
            .isInstanceOf(TradinException.class)
            .satisfies(e -> {
                TradinException te = (TradinException) e;
                assertThat(te.getErrorType()).isEqualTo(SAME_POSITION_REQUEST_EXCEPTION);
            });
    }

    @Test
    void 웹훅_처리시_오픈된_히스토리가_없으면_예외_발생() throws Exception {
        // given
        Strategy strategy = StrategyFixture.get();
        Position position = PositionFixture.getDifferent(strategy.getCurrentPosition()); // 다른 포지션 가정
        WebHookDto dto = WebHookDto.of(strategy.getId(), position);

        when(strategyReader.findStrategyById(anyLong())).thenReturn(strategy);
        when(historyReader.findOpenHistoryByStrategyId(anyLong()))
            .thenThrow(new TradinException(NOT_FOUND_HISTORY_EXCEPTION));

        // when & then
        assertThatThrownBy(() -> strategyService.handleFutureWebHook(dto))
            .isInstanceOf(TradinException.class)
            .satisfies(e -> {
                TradinException te = (TradinException) e;
                assertThat(te.getErrorType()).isEqualTo(NOT_FOUND_HISTORY_EXCEPTION);
            });
    }
}