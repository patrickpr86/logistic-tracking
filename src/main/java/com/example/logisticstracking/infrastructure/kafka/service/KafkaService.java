package com.example.logisticstracking.infrastructure.kafka.service;

import com.example.logisticstracking.infrastructure.kafka.dto.TrackingEventKafkaDTO;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

@Slf4j
@Service
public class KafkaService {

    private final KafkaTemplate<String, TrackingEventKafkaDTO> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, TrackingEventKafkaDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<Void> sendTrackingEvent(TrackingEventKafkaDTO event) {
        return kafkaTemplate.send(DEFAULT_TRACKING_TOPIC, event.getTrackingId(), event)
                .thenAccept(result -> log.info(LOG_PACKAGE_EVENT_SENT_SUCCESS_TEMPLATE, event.getTrackingId(), event))
                .exceptionally(ex -> {
                    log.error(LOG_PACKAGE_EVENT_SEND_FAILURE_TEMPLATE, event.getTrackingId(), event, ex.getMessage(), ex);
                    return null;
                });
    }

}
