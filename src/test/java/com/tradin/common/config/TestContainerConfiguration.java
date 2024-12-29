package com.tradin.common.config;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerConfiguration implements BeforeAllCallback {

    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;

    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:13.2");
    private static final String POSTGRES_USERNAME = "test";
    private static final String POSTGRES_PASSWORD = "test";
    private static final String POSTGRES_DB = "test";
    private static final int POSTGRES_PORT = 5432;

    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:latest");

    @Override
    public void beforeAll(ExtensionContext context) {
        startRedis();
        startPostgresql();
        startKafka();
    }

    private void startKafka() {
        KafkaContainer kafka = new KafkaContainer(KAFKA_IMAGE);
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
    }

    private void startPostgresql() {
        PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(POSTGRES_IMAGE).withUsername(POSTGRES_USERNAME).withPassword(POSTGRES_PASSWORD).withDatabaseName(POSTGRES_DB);
        postgresql.start();
        System.setProperty("spring.datasource.url", postgresql.getJdbcUrl());
        System.setProperty("spring.datasource.username", POSTGRES_USERNAME);
        System.setProperty("spring.datasource.password", POSTGRES_PASSWORD);
    }

    private void startRedis() {
        GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE)).withExposedPorts(REDIS_PORT);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redis.getMappedPort(REDIS_PORT)));
    }
}