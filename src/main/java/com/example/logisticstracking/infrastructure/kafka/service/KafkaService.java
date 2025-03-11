package com.example.logisticstracking.infrastructure.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTrackingEvent(String eventDescription) {
        log.info("Enviando mensagem para Kafka: {}", eventDescription);
        kafkaTemplate.send("tracking-events", eventDescription);
    }
}
