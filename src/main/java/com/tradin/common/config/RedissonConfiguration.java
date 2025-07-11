package com.tradin.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfiguration {

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        config.useSingleServer()
            .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
            .setConnectionPoolSize(64)
            .setConnectionMinimumIdleSize(10)
            .setTimeout(3000);
        return Redisson.create(config);
    }
}