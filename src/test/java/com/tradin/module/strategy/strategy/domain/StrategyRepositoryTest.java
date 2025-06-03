package com.tradin.module.strategy.strategy.domain;

import com.tradin.common.test.BaseRepositoryTest;

class StrategyRepositoryTest extends BaseRepositoryTest {

//    @Autowired
//    private StrategyRepository strategyRepository;
//
//    private Strategy testStrategy;
//
//    @BeforeEach
//    void setUp() {
//        // 테스트 데이터 초기화
//        testStrategy = Strategy.builder()
//            .name("Test Strategy")
//            .strategyType(StrategyType.MOMENTUM)
//            .tradingType(TradingType.FUTURES)
//            .coinType(CoinType.BTC)
//            .timeFrameType(TimeFrameType.MINUTE_5)
//            .position(Position.LONG)
//            .rate(new Rate(10.0))
//            .count(new Count(100L))
//            .build();
//
//        strategyRepository.save(testStrategy);
//        flushAndClear();
//    }
//
//    @Test
//    @DisplayName("전략 저장 - 성공")
//    void save_Success() {
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
//        // when
//        Strategy saved = strategyRepository.save(newStrategy);
//        flushAndClear();
//
//        // then
//        assertThat(saved.getId()).isNotNull();
//        assertThat(saved.getName()).isEqualTo("New Strategy");
//        assertThat(saved.getStrategyType()).isEqualTo(StrategyType.ARBITRAGE);
//    }
//
//    @Test
//    @DisplayName("전략 조회 - ID로 조회")
//    void findById_Success() {
//        // when
//        Strategy found = strategyRepository.findById(testStrategy.getId()).orElse(null);
//
//        // then
//        assertThat(found).isNotNull();
//        assertThat(found.getName()).isEqualTo(testStrategy.getName());
//        assertThat(found.getStrategyType()).isEqualTo(testStrategy.getStrategyType());
//    }
//
//    @Test
//    @DisplayName("전략 목록 조회 - 전체 조회")
//    void findAll_Success() {
//        // given
//        Strategy anotherStrategy = Strategy.builder()
//            .name("Another Strategy")
//            .strategyType(StrategyType.MEAN_REVERSION)
//            .tradingType(TradingType.FUTURES)
//            .coinType(CoinType.ETH)
//            .timeFrameType(TimeFrameType.DAY_1)
//            .position(Position.LONG)
//            .rate(new Rate(15.0))
//            .count(new Count(200L))
//            .build();
//        strategyRepository.save(anotherStrategy);
//        flushAndClear();
//
//        // when
//        List<Strategy> strategies = strategyRepository.findAll();
//
//        // then
//        assertThat(strategies).hasSize(2);
//        assertThat(strategies).extracting(Strategy::getName)
//            .containsExactlyInAnyOrder("Test Strategy", "Another Strategy");
//    }
//
//    @Test
//    @DisplayName("전략 수정 - 성공")
//    void update_Success() {
//        // given
//        Strategy found = strategyRepository.findById(testStrategy.getId()).orElseThrow();
//
//        // when
//        found.updateName("Updated Strategy");
//        found.updateRate(new Rate(20.0));
//        flushAndClear();
//
//        // then
//        Strategy updated = strategyRepository.findById(testStrategy.getId()).orElseThrow();
//        assertThat(updated.getName()).isEqualTo("Updated Strategy");
//        assertThat(updated.getRate().getValue()).isEqualTo(20.0);
//    }
//
//    @Test
//    @DisplayName("전략 삭제 - 성공")
//    void delete_Success() {
//        // when
//        strategyRepository.deleteById(testStrategy.getId());
//        flushAndClear();
//
//        // then
//        assertThat(strategyRepository.findById(testStrategy.getId())).isEmpty();
//    }
//
//    @Test
//    @DisplayName("전략 조회 - 전략 타입별 조회")
//    void findByStrategyType_Success() {
//        // given
//        Strategy momentumStrategy = Strategy.builder()
//            .name("Momentum Strategy 2")
//            .strategyType(StrategyType.MOMENTUM)
//            .tradingType(TradingType.SPOT)
//            .coinType(CoinType.ETH)
//            .timeFrameType(TimeFrameType.MINUTE_30)
//            .position(Position.LONG)
//            .rate(new Rate(8.0))
//            .count(new Count(80L))
//            .build();
//        strategyRepository.save(momentumStrategy);
//        flushAndClear();
//
//        // when
//        List<Strategy> momentumStrategies = strategyRepository.findByStrategyType(StrategyType.MOMENTUM);
//
//        // then
//        assertThat(momentumStrategies).hasSize(2);
//        assertThat(momentumStrategies).allMatch(s -> s.getStrategyType() == StrategyType.MOMENTUM);
//    }
}