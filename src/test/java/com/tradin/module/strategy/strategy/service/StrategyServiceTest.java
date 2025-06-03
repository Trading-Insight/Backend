package com.tradin.module.strategy.strategy.service;

import com.tradin.common.test.BaseServiceTest;

class StrategyServiceTest extends BaseServiceTest {

//    @Mock
//    private StrategyReader strategyReader;
//
//    @Mock
//    private StrategyProcessor strategyProcessor;
//
//    @InjectMocks
//    private StrategyService strategyService;
//
//    private Strategy testStrategy;
//
//    @BeforeEach
//    void setUp() {
//        testStrategy = Strategy.builder()
//            .id(1L)
//            .name("Test Strategy")
//            .strategyType(StrategyType.MOMENTUM)
//            .tradingType(TradingType.FUTURES)
//            .coinType(CoinType.BTC)
//            .timeFrameType(TimeFrameType.MINUTE_5)
//            .position(Position.LONG)
//            .rate(new Rate(10.0))
//            .count(new Count(100L))
//            .build();
//    }
//
//    @Test
//    @DisplayName("전략 목록 조회 - 성공")
//    void findAllStrategies_Success() {
//        // given
//        List<Strategy> strategies = List.of(testStrategy);
//        given(strategyReader.findAll()).willReturn(strategies);
//
//        // when
//        var result = strategyService.findAllStrategies();
//
//        // then
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).getName()).isEqualTo(testStrategy.getName());
//
//        then(strategyReader).should(times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("전략 상세 조회 - 성공")
//    void findStrategyById_Success() {
//        // given
//        given(strategyReader.findById(1L)).willReturn(testStrategy);
//
//        // when
//        var result = strategyService.findStrategyById(1L);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo(testStrategy.getId());
//        assertThat(result.getName()).isEqualTo(testStrategy.getName());
//
//        then(strategyReader).should(times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("전략 상세 조회 - 전략 없음")
//    void findStrategyById_NotFound() {
//        // given
//        given(strategyReader.findById(anyLong())).willThrow(new TradinException("전략을 찾을 수 없습니다"));
//
//        // when & then
//        assertThatThrownBy(() -> strategyService.findStrategyById(99L))
//            .isInstanceOf(TradinException.class)
//            .hasMessageContaining("전략을 찾을 수 없습니다");
//
//        then(strategyReader).should(times(1)).findById(99L);
//    }
//
//    @Test
//    @DisplayName("웹훅 실행 - 성공")
//    void executeWebhook_Success() {
//        // given
//        WebHookDto webhookDto = WebHookDto.builder()
//            .action("BUY")
//            .symbol("BTCUSDT")
//            .price(50000.0)
//            .build();
//
//        willDoNothing().given(strategyProcessor).processWebhook(any(WebHookDto.class));
//
//        // when
//        strategyService.executeWebhook(webhookDto);
//
//        // then
//        then(strategyProcessor).should(times(1)).processWebhook(webhookDto);
//    }
//
//    @Test
//    @DisplayName("전략 생성 - 성공")
//    void createStrategy_Success() {
//        // given
//        Strategy newStrategy = Strategy.builder()
//            .name("New Strategy")
//            .strategyType(StrategyType.ARBITRAGE)
//            .tradingType(TradingType.SPOT)
//            .coinType(CoinType.ETH)
//            .timeFrameType(TimeFrameType.HOUR_1)
//            .position(Position.SHORT)
//            .rate(new Rate(5.0))
//            .count(new Count(50L))
//            .build();
//
//        given(strategyProcessor.create(any(Strategy.class))).willReturn(newStrategy);
//
//        // when
//        var result = strategyService.createStrategy(newStrategy);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getName()).isEqualTo(newStrategy.getName());
//
//        then(strategyProcessor).should(times(1)).create(any(Strategy.class));
//    }
//
//    @Test
//    @DisplayName("전략 삭제 - 성공")
//    void deleteStrategy_Success() {
//        // given
//        given(strategyReader.findById(1L)).willReturn(testStrategy);
//        willDoNothing().given(strategyProcessor).delete(any(Strategy.class));
//
//        // when
//        strategyService.deleteStrategy(1L);
//
//        // then
//        then(strategyReader).should(times(1)).findById(1L);
//        then(strategyProcessor).should(times(1)).delete(testStrategy);
//    }
}