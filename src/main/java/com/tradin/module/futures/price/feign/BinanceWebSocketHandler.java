package com.tradin.module.futures.price.feign;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tradin.module.futures.price.domain.PriceCache;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Slf4j
@Component
@RequiredArgsConstructor
public class BinanceWebSocketHandler {

    private static final String STREAM_URL = "wss://fstream.binance.com/stream?streams=btcusdt@markPrice@1s/ethusdt@markPrice@1s";

    private final PriceCache priceCache;

    @PostConstruct
    public void startWebSocket() {
        try {
            WebSocketClient client = new StandardWebSocketClient();

            client.execute(new TextWebSocketHandler() {
                @Override
                public void handleTextMessage(WebSocketSession session, TextMessage message) {
                    try {
                        JsonObject json = JsonParser.parseString(message.getPayload()).getAsJsonObject();
                        JsonObject data = json.getAsJsonObject("data");

                        String symbol = data.get("s").getAsString();
                        BigDecimal price = new BigDecimal(data.get("p").getAsString());

                        priceCache.updatePrice(symbol, price);

                        //priceCache.getAllPrices().forEach((sym, prc) -> log.info("[현재 시세] {}: {}", sym, prc));
                    } catch (Exception e) {
                        log.error("[WebSocket 파싱 오류] {}", e.getMessage());
                    }
                }

                @Override
                public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
                    log.warn("[WebSocket 연결 종료] 상태: {}", status);
                }

                @Override
                public void handleTransportError(WebSocketSession session, Throwable exception) {
                    log.error("[WebSocket 전송 오류] {}", exception.getMessage());
                }
            }, STREAM_URL);

            log.info("[WebSocket 연결 시도] {}", STREAM_URL);
        } catch (Exception e) {
            log.error("[WebSocket 실행 중 오류 발생]: {}", e.getMessage());
        }
    }

}