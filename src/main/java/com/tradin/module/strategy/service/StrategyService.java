package com.tradin.module.strategy.service;

import com.tradin.common.utils.AESUtils;
import com.tradin.module.futures.order.feign.service.BinanceFeignService;
import com.tradin.module.futures.order.service.FuturesOrderService;
import com.tradin.module.history.service.HistoryService;
import com.tradin.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.tradin.module.strategy.domain.TradingType;
import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.implement.StrategyReader;
import com.tradin.module.users.service.UsersService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class StrategyService {

    private final HistoryService historyService;
    private final BinanceFeignService binanceFeignService;
    private final UsersService userService;
    private final FuturesOrderService futuresOrderService;
    private final AESUtils aesUtils;
    private final StrategyReader strategyReader;

    public FindStrategiesInfoResponseDto findFutureStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = strategyReader.findFutureStrategyInfoDaos();
        return FindStrategiesInfoResponseDto.of(strategiesInfo);
    }

    public FindStrategiesInfoResponseDto findSpotStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = strategyReader.findSpotStrategyInfoDaos();
        return FindStrategiesInfoResponseDto.of(strategiesInfo);
    }

//    private static TradingType webHookTradingType(WebHookDto request) {
//        return request.getPosition().getTradingType();
//    }
//
//    public FindSubscriptionStrategiesInfoResponseDto findSubscriptionStrategiesInfo() {
//        List<SubscriptionStrategyInfoDao> subscriptionStrategyInfo = strategyReader.findSubscriptionStrategyInfoDaos();
//        return new FindSubscriptionStrategiesInfoResponseDto(subscriptionStrategyInfo);
//    }
//
//
//    public void subscribeStrategy(SubscribeStrategyDto request) {
//        Users savedUser = getUserFromSecurityContext();
//        Strategy strategy = strategyReader.findById(request.getId());
//        String encryptedApiKey = getEncryptedKey(request.getBinanceApiKey());
//        String encryptedSecretKey = getEncryptedKey(request.getBinanceSecretKey());
//
//        savedUser.subscribeStrategy(strategy, encryptedApiKey, encryptedSecretKey, request.getLeverage(), request.getQuantityRate(), request.getTradingType());
//    }
//
//    public void unsubscribeStrategy(UnSubscribeStrategyDto request) {
//        Users savedUser = getUserFromSecurityContext();
//        Strategy strategy = strategyReader.findById(request.getId());
//
//        isUserSubscribedStrategy(savedUser, strategy);
//
//        if (request.isPositionClose() && isUserPositionExist(savedUser.getCurrentPositionType())) {
//            String side = getSideFromUserCurrentPosition(savedUser);
//            closePosition(savedUser.getBinanceApiKey(), savedUser.getBinanceSecretKey(), side);
//        }
//
//        savedUser.unsubscribeStrategy();
//    }
//
//    private static void isUserSubscribedStrategy(Users users, Strategy strategy) {
//        if (!users.getStrategy().getId().equals(strategy.getId())) {
//            throw new TradinException(NOT_SUBSCRIBED_STRATEGY_EXCEPTION);
//        }
//    }
//
//    private void autoTrading(String name, TradingType tradingType) {
//        tradeService.autoTrading(name, tradingType);
//    }
//
//    private String getSideFromUserCurrentPosition(Users savedUser) {
//        return savedUser.getCurrentPositionType().equals(LONG) ? "SELL" : "BUY";
//    }
//
//    private void closePosition(String apiKey, String secretKey, String side) {
//        binanceFeignService.closePosition(apiKey, secretKey, side);
//    }
//
//    private String getEncryptedKey(String key) {
//        return aesUtils.encrypt(key);
//    }
//
//    private Users getUserFromSecurityContext() {
//        return userService.getUserFromSecurityContext();
//    }
//
//
//    private void closeOngoingHistory(Strategy strategy, Position exitPosition) {
//        historyService.closeOngoingHistory(strategy, exitPosition);
//    }
//
//    private void createNewHistory(Strategy strategy, Position position) {
//        historyService.createNewHistory(strategy, position);
//    }
//
//    private void evictHistoryCache(Long strategyId) {
//        historyService.evictHistoryCache(strategyId);
//    }
//
//    private void updateStrategyMetaData(Strategy strategy, Position position) {
//        strategy.updateMetaData(position);
//    }

//    private List<SubscriptionStrategyInfoDao> findSubscriptionStrategyInfoDaos() {
//        return strategyRepository.findSubscriptionStrategiesInfoDao()
//                .orElse(Collections.emptyList());
//    }


    private boolean isUserPositionExist(TradingType tradingType) {
        return tradingType != TradingType.NONE;
    }

//    public void handleFutureWebHook(WebHookDto request) {
//        Strategy strategy = findByName(request.getName());
//        String strategyName = strategy.getName();
//        TradingType strategyCurrentPosition = strategy.getCurrentPosition().getTradingType();
//
////      autoTrading(strategyName, strategyCurrentPosition);
//        closeOngoingHistory(strategy, request.getPosition());
//        createNewHistory(strategy, request.getPosition());
//        evictHistoryCache(strategy.getId());
//        updateStrategyMetaData(strategy, request.getPosition());
//    }
//
//    public void handleSpotWebHook(WebHookDto request) {
//        Strategy strategy = findByName(request.getName());
//
//        if (webHookTradingType(request) == LONG) {
//            createNewHistory(strategy, request.getPosition());
//            return;
//        }
//
//        closeOngoingHistory(strategy, request.getPosition());
//        evictHistoryCache(strategy.getId());
//        updateStrategyMetaData(strategy, request.getPosition());
//    }
}
