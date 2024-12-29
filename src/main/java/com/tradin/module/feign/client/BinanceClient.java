package com.tradin.module.feign.client;

import com.tradin.module.feign.client.dto.binance.ChangeLeverageDto;
import com.tradin.module.feign.client.dto.binance.CurrentPositionInfoDto;
import com.tradin.module.feign.client.dto.binance.FutureAccountBalanceDto;
import com.tradin.module.feign.client.dto.binance.NewOrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "BinanceClient", url = "https://testnet.binancefuture.com")
public interface BinanceClient {

    @GetMapping("/fapi/v2/positionRisk")
    List<CurrentPositionInfoDto> getCurrentPositionInfo(
            @RequestParam("symbol") String symbol,
            @RequestParam("timestamp") Long timestamp,
            @RequestHeader("X-MBX-APIKEY") String apiKey,
            @RequestParam("signature") String signature);

    @PostMapping("/fapi/v1/order")
    NewOrderDto order(
            @RequestHeader("X-MBX-APIKEY") String apiKey,
            @RequestParam("quantity") double quantity,
            @RequestParam("side") String side,
            @RequestParam("signature") String signature,
            @RequestParam("symbol") String symbol,
            @RequestParam("timestamp") Long timestamp,
            @RequestParam("type") String type
    );

    @PostMapping("/fapi/v1/leverage")
    ChangeLeverageDto changeLeverage(
            @RequestHeader("X-MBX-APIKEY") String apiKey,
            @RequestParam("leverage") int leverage,
            @RequestParam("symbol") String symbol,
            @RequestParam("timestamp") Long timestamp,
            @RequestParam("signature") String signature
    );

    @GetMapping("/fapi/v2/balance")
    List<FutureAccountBalanceDto> getFutureAccountBalance(
            @RequestHeader("X-MBX-APIKEY") String apiKey,
            @RequestParam("timestamp") Long timestamp,
            @RequestParam("signature") String signature
    );
}
