package com.example.logisticstracking.infrastructure.kafka.utils;

import com.example.logisticstracking.domain.enumeration.PackageStatus;
import com.example.logisticstracking.infrastructure.kafka.dto.TrackingEventKafkaDTO;
import com.example.logisticstracking.infrastructure.kafka.service.KafkaService;
import org.slf4j.Logger;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;
import static com.example.logisticstracking.domain.constants.PackageConstants.LOG_PACKAGE_EVENT_SEND_FAILURE_TEMPLATE;

public class SendMessageTemplate {
    public static void publishPackageStatusUpdate(String packageId, PackageStatus newStatus, Logger log, KafkaService kafkaService) {
        TrackingEventKafkaDTO event = new TrackingEventKafkaDTO(
                newStatus,
                packageId,
                PACKAGE_STATUS_UPDATED_KAFKA_MESSAGE.formatted(packageId, newStatus.name())
        );

        log.info(LOG_SENDING_PACKAGE_EVENT_TO_KAFKA_TEMPLATE, event.getStatus(), packageId);

        kafkaService.sendTrackingEvent(event)
                .thenRun(() -> log.info(LOG_PACKAGE_UPDATED_KAFKA_TEMPLATE, packageId, newStatus))
                .exceptionally(ex -> {
                    log.error(LOG_PACKAGE_EVENT_SEND_FAILURE_TEMPLATE, packageId, event, ex.getMessage(), ex);
                    return null;
                });
    }
}
