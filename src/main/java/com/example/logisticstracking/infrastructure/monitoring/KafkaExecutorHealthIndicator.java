package com.example.logisticstracking.infrastructure.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

@Component
public class KafkaExecutorHealthIndicator implements HealthIndicator {

    private final ThreadPoolTaskExecutor kafkaEventExecutor;

    public KafkaExecutorHealthIndicator(ThreadPoolTaskExecutor kafkaEventExecutor) {
        this.kafkaEventExecutor = kafkaEventExecutor;
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail(ACTIVE_THREADS, kafkaEventExecutor.getActiveCount())
                .withDetail(MAX_THREADS, kafkaEventExecutor.getMaxPoolSize())
                .withDetail(QUEUE_SIZE, kafkaEventExecutor.getThreadPoolExecutor().getQueue().size())
                .build();
    }
}
