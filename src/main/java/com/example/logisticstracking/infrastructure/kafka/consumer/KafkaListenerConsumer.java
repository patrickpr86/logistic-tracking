package com.example.logisticstracking.infrastructure.kafka.consumer;

import com.example.logisticstracking.infrastructure.kafka.configuration.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListenerConsumer {

    @KafkaListener(topics = KafkaConfig.TRACKING_TOPIC, groupId = "logistics-consumer")
    public void consumeMessage(String message) {
        log.info("Consumindo mensagem do Kafka: {}", message);
    }
}

