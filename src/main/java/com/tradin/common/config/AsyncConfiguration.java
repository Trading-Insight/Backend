package com.tradin.common.config;

import com.tradin.common.exception.AsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration extends AsyncConfigurerSupport {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); //기본적으로 실행을 대기하고 있는 쓰레드의 갯수
        executor.setMaxPoolSize(30); //최대 쓰레드 풀 사이즈
        executor.setQueueCapacity(50); //쓰레드가 모두 사용중인 경우 큐에 쌓여있는 요청의 최대 갯수
        executor.setThreadNamePrefix("TRADIN-ASYNC-"); //쓰레드 이름 접두사
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}
