package com.tradin.core.autotrade.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.core.autotrade.service.AutoTradeFacadeService;
import com.tradin.core.autotrade.service.dto.AutoTradeEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoTradeBatchEventListener {

    private final AutoTradeFacadeService autoTradeFacadeService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "auto-trade-topic",
        groupId = "auto-trade-group",
        concurrency = "3",
        batch = "true"
    )
    @Transactional
    public void listenBatch(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        ack.acknowledge();

        if (records.isEmpty()) {
            return;
        }

        List<AutoTradeEventDto> events = parseEvents(records);
        autoTradeFacadeService.processBatchEvents(events);
    }

    private List<AutoTradeEventDto> parseEvents(List<ConsumerRecord<String, String>> records) {
        return records.stream()
            .map(this::parseEvent)
            .toList();
    }

    private AutoTradeEventDto parseEvent(ConsumerRecord<String, String> record) {
        try {
            return objectMapper.readValue(record.value(), AutoTradeEventDto.class);
        } catch (Exception e) {
            log.error("이벤트 파싱 실패: record={}, error={}", record.value(), e.getMessage());
            throw new RuntimeException("이벤트 파싱 실패", e);
        }
    }
}