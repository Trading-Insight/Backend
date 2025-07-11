package com.tradin.common.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "tradeExecutor")
    public Executor tradeExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
