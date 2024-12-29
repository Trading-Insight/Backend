package com.tradin.module.strategy.service;

import com.tradin.common.exception.ExceptionMessage;
import com.tradin.common.exception.TradinException;
import com.tradin.common.utils.AESUtils;
import com.tradin.module.feign.service.BinanceFeignService;
import com.tradin.module.history.service.HistoryService;
import com.tradin.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.tradin.module.strategy.controller.dto.response.FindSubscriptionStrategiesInfoResponseDto;
import com.tradin.module.strategy.domain.Position;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.strategy.domain.TradingType;
import com.tradin.module.strategy.domain.repository.StrategyRepository;
import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.domain.repository.dao.SubscriptionStrategyInfoDao;
import com.tradin.module.strategy.service.dto.UnSubscribeStrategyDto;
import com.tradin.module.strategy.service.dto.WebHookDto;
import com.tradin.module.trade.service.TradeService;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.service.UsersService;
import com.tradin.module.users.service.dto.SubscribeStrategyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.tradin.common.exception.ExceptionMessage.NOT_SUBSCRIBED_STRATEGY_EXCEPTION;
import static com.tradin.module.strategy.domain.TradingType.LONG;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StrategyService {
    private final HistoryService historyService;
    private final BinanceFeignService binanceFeignService;
    private final UsersService userService;
    private final TradeService tradeService;
    private final StrategyRepository strategyRepository;
    private final AESUtils aesUtils;

    public void handleFutureWebHook(WebHookDto request) {
        Strategy strategy = findByName(request.getName());
        String strategyName = strategy.getName();
        TradingType strategyCurrentPosition = strategy.getCurrentPosition().getTradingType();

//      autoTrading(strategyName, strategyCurrentPosition);
        closeOngoingHistory(strategy, request.getPosition());
        createNewHistory(strategy, request.getPosition());
        evictHistoryCache(strategy.getId());
        updateStrategyMetaData(strategy, request.getPosition());
    }

    public void handleSpotWebHook(WebHookDto request) {
        Strategy strategy = findByName(request.getName());

        if (webHookTradingType(request) == LONG) {
            createNewHistory(strategy, request.getPosition());
            return;
        }

        closeOngoingHistory(strategy, request.getPosition());
        evictHistoryCache(strategy.getId());
        updateStrategyMetaData(strategy, request.getPosition());
    }

    private static TradingType webHookTradingType(WebHookDto request) {
        return request.getPosition().getTradingType();
    }

    public FindSubscriptionStrategiesInfoResponseDto findSubscriptionStrategiesInfo() {
        List<SubscriptionStrategyInfoDao> subscriptionStrategyInfo = findSubscriptionStrategyInfoDaos();
        return new FindSubscriptionStrategiesInfoResponseDto(subscriptionStrategyInfo);
    }

    public FindStrategiesInfoResponseDto findFutureStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = findFutureStrategyInfoDaos();
        return new FindStrategiesInfoResponseDto(strategiesInfo);
    }

    public FindStrategiesInfoResponseDto findSpotStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = findSpotStrategyInfoDaos();
        return new FindStrategiesInfoResponseDto(strategiesInfo);
    }


    public void subscribeStrategy(SubscribeStrategyDto request) {
        Users savedUser = getUserFromSecurityContext();
        Strategy strategy = findById(request.getId());
        String encryptedApiKey = getEncryptedKey(request.getBinanceApiKey());
        String encryptedSecretKey = getEncryptedKey(request.getBinanceSecretKey());

        savedUser.subscribeStrategy(strategy, encryptedApiKey, encryptedSecretKey, request.getLeverage(), request.getQuantityRate(), request.getTradingType());
    }

    public void unsubscribeStrategy(UnSubscribeStrategyDto request) {
        Users savedUser = getUserFromSecurityContext();
        Strategy strategy = findById(request.getId());

        isUserSubscribedStrategy(savedUser, strategy);

        if (request.isPositionClose() && isUserPositionExist(savedUser.getCurrentPositionType())) {
            String side = getSideFromUserCurrentPosition(savedUser);
            closePosition(savedUser.getBinanceApiKey(), savedUser.getBinanceSecretKey(), side);
        }

        savedUser.unsubscribeStrategy();
    }

    private static void isUserSubscribedStrategy(Users users, Strategy strategy) {
        if (!users.getStrategy().getId().equals(strategy.getId())) {
            throw new TradinException(NOT_SUBSCRIBED_STRATEGY_EXCEPTION);
        }
    }

    private void autoTrading(String name, TradingType tradingType) {
        tradeService.autoTrading(name, tradingType);
    }

    private String getSideFromUserCurrentPosition(Users savedUser) {
        return savedUser.getCurrentPositionType().equals(LONG) ? "SELL" : "BUY";
    }

    private void closePosition(String apiKey, String secretKey, String side) {
        binanceFeignService.closePosition(apiKey, secretKey, side);
    }

    private String getEncryptedKey(String key) {
        return aesUtils.encrypt(key);
    }

    private Users getUserFromSecurityContext() {
        return userService.getUserFromSecurityContext();
    }

    private Strategy findById(Long id) {
        return strategyRepository.findById(id)
                .orElseThrow(() -> new TradinException(ExceptionMessage.NOT_FOUND_STRATEGY_EXCEPTION));
    }

    private void closeOngoingHistory(Strategy strategy, Position exitPosition) {
        historyService.closeOngoingHistory(strategy, exitPosition);
    }

    private void createNewHistory(Strategy strategy, Position position) {
        historyService.createNewHistory(strategy, position);
    }

    private void evictHistoryCache(Long strategyId) {
        historyService.evictHistoryCache(strategyId);
    }

    private void updateStrategyMetaData(Strategy strategy, Position position) {
        strategy.updateMetaData(position);
    }

    private Strategy findByName(String name) {
        return strategyRepository.findByName(name)
                .orElseThrow(() -> new TradinException(ExceptionMessage.NOT_FOUND_STRATEGY_EXCEPTION));
    }

    private List<StrategyInfoDao> findFutureStrategyInfoDaos() {
        return strategyRepository.findFutureStrategiesInfoDao();
    }

//    private List<SubscriptionStrategyInfoDao> findSubscriptionStrategyInfoDaos() {
//        return strategyRepository.findSubscriptionStrategiesInfoDao()
//                .orElse(Collections.emptyList());
//    }

    private List<StrategyInfoDao> findSpotStrategyInfoDaos() {
        return strategyRepository.findSpotStrategiesInfoDao();
    }

    private List<SubscriptionStrategyInfoDao> findSubscriptionStrategyInfoDaos() {
        return strategyRepository.findSubscriptionStrategiesInfoDao()
                .orElse(Collections.emptyList());
    }

    private boolean isUserPositionExist(TradingType tradingType) {
        return tradingType != TradingType.NONE;
    }

}
