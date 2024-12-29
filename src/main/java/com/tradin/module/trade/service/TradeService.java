package com.tradin.module.trade.service;

import com.tradin.common.utils.AESUtils;
import com.tradin.module.feign.service.BinanceFeignService;
import com.tradin.module.strategy.domain.TradingType;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TradeService {
    private final UsersService userService;
    private final BinanceFeignService binanceFeignService;
    private final AESUtils aesUtils;

    public void autoTrading(String strategyName, TradingType strategyCurrentPosition) {
        List<Users> autoTradingSubscribers = userService.findAutoTradingSubscriberByStrategyName(
                strategyName);

        autoTradingSubscribers.forEach(user -> {
            String apiKey = getDecryptedKey(user.getBinanceApiKey());
            String secretKey = getDecryptedKey(user.getBinanceSecretKey());

            trade(user, strategyCurrentPosition, apiKey, secretKey);
        });
    }

    private String getDecryptedKey(String encryptedKey) {
        return aesUtils.decrypt(encryptedKey);
    }

    private void trade(Users user, TradingType strategyCurrentPosition, String apiKey, String secretKey) {

        if (strategyCurrentPosition == TradingType.LONG) {
            processLongPosition(apiKey, secretKey, "SELL", user);
        } else if (strategyCurrentPosition == TradingType.SHORT) {
            processShortPosition(apiKey, secretKey, "BUY", user);
        }

    }


    private void processLongPosition(String apikey, String secretKey, String side, Users user) {
        if (isUserTradingTypeContainsShort(user)) {
            int orderQuantity = calculateOrderQuantity(apikey, secretKey, user.getLeverage(), user.getQuantityRate());

            if (isUserPositionExist(user.getCurrentPositionType())) {
                switchAndChangeCurrentPosition(apikey, secretKey, side, orderQuantity, user, TradingType.SHORT);
            } else {
                openAndChangeCurrentPosition(apikey, secretKey, side, orderQuantity, user, TradingType.SHORT);
            }
        } else {
            closeAndChangeCurrentPosition(apikey, secretKey, side, user, TradingType.NONE);
        }
    }

    private void processShortPosition(String apikey, String secretKey, String side, Users user) {
        if (isUserTradingTypeContainsLong(user)) {
            int orderQuantity = calculateOrderQuantity(apikey, secretKey, user.getLeverage(), user.getQuantityRate());

            if (isUserPositionExist(user.getCurrentPositionType())) {
                switchAndChangeCurrentPosition(apikey, secretKey, side, orderQuantity, user, TradingType.LONG);
            } else {
                openAndChangeCurrentPosition(apikey, secretKey, side, orderQuantity, user, TradingType.LONG);
            }
        } else {
            closeAndChangeCurrentPosition(apikey, secretKey, side, user, TradingType.NONE);
        }
    }

    private void switchAndChangeCurrentPosition(String apikey, String secretKey, String side, int orderQuantity, Users user, TradingType type) {
        switchPosition(apikey, secretKey, side, orderQuantity);
        changeCurrentPosition(user, type);
    }

    private void closeAndChangeCurrentPosition(String apikey, String secretKey, String side, Users user, TradingType type) {
        closePosition(apikey, secretKey, side);
        changeCurrentPosition(user, type);
    }

    private void openAndChangeCurrentPosition(String apikey, String secretKey, String side, int orderQuantity, Users user, TradingType type) {
        openPosition(apikey, secretKey, side, orderQuantity);
        changeCurrentPosition(user, type);
    }

    private void switchPosition(String apiKey, String secretKey, String side, int orderQuantity) {
        binanceFeignService.openPosition(apiKey, secretKey, side, orderQuantity);
    }

    private void closePosition(String apiKey, String secretKey, String side) {
        binanceFeignService.closePosition(apiKey, secretKey, side);
    }

    private void openPosition(String apiKey, String secretKey, String side, int orderQuantity) {
        binanceFeignService.openPosition(apiKey, secretKey, side, orderQuantity);
    }

    private static void changeCurrentPosition(Users user, TradingType tradingType) {
        user.changeCurrentPosition(tradingType);
    }

    private int calculateOrderQuantity(String apiKey, String secretKey, int leverage, int quantityRate) {
        int futureAccountBalance = binanceFeignService.getFutureAccountBalance(apiKey, secretKey);
        return futureAccountBalance * leverage * quantityRate / 100;
    }

    private boolean isUserPositionExist(TradingType tradingType) {
        return tradingType != TradingType.NONE;
    }

    private boolean isUserTradingTypeContainsLong(Users user) {
        return user.getTradingType() == TradingType.LONG || user.getTradingType() == TradingType.BOTH;
    }

    private boolean isUserTradingTypeContainsShort(Users user) {
        return user.getTradingType() == TradingType.SHORT || user.getTradingType() == TradingType.BOTH;
    }

}
