package com.example.logisticstracking.infrastructure.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.Executor;

@Slf4j
@Component
public class KafkaListenerConsumer {

    private final Executor kafkaEventExecutor;

    @Value("${kafka.tracking.topic-name}")
    private String trackingTopicName;

    public KafkaListenerConsumer(@Qualifier("kafkaEventExecutor") Executor kafkaEventExecutor) {
        this.kafkaEventExecutor = kafkaEventExecutor;
    }

    /**
     * Listens to Kafka topic and processes messages asynchronously using a dedicated thread pool.
     *
     * @param message The Kafka message.
     */
    @KafkaListener(
            topics = "#{T(org.springframework.core.env.Environment).getProperty('kafka.tracking.topic-name')}",
            groupId = "logistics-consumer",
            concurrency = "3"
    )
    public void consumeMessage(String message) {
        log.info("Received message from Kafka topic {}: {}", trackingTopicName, message);

        kafkaEventExecutor.execute(() -> {
            try {
                log.info("Processing Kafka message: {}", message);
                log.info("Message processed successfully: {}", message);
            } catch (Exception e) {
                log.error("Error processing Kafka message: {}", message, e);
            }
        });
    }
}
