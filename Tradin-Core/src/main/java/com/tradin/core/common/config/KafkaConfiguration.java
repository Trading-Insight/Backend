package com.tradin.core.common.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public static final String AUTO_TRADE_TOPIC = "auto-trade-topic";
    public static final String AUTO_TRADE_RETRY_TOPIC = "auto-trade-retry-topic";


    @Bean
    public KafkaAdmin kafkaAdmin() { //TODO
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic autoTradeTopic() {
        return TopicBuilder.name(AUTO_TRADE_TOPIC)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic autoTradeRetryTopic() {
        return TopicBuilder.name(AUTO_TRADE_RETRY_TOPIC)
            .partitions(1)
            .replicas(1)
            .build();
    }
} 