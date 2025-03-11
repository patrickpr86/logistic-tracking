package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.domain.entity.Package;
import com.example.logisticstracking.domain.enumeration.PackageStatus;
import com.example.logisticstracking.domain.exception.InvalidStatusTransitionException;
import com.example.logisticstracking.domain.exception.PackageNotFoundException;
import com.example.logisticstracking.infrastructure.kafka.service.KafkaService;
import com.example.logisticstracking.infrastructure.mapper.PackageMapper;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import com.example.logisticstracking.infrastructure.persistence.repository.TrackingEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

@Slf4j
@Service
public class CancelPackageUseCase {

    private final PackageRepository packageRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final KafkaService kafkaService;

    public CancelPackageUseCase(
            PackageRepository packageRepository,
            TrackingEventRepository trackingEventRepository,
            KafkaService kafkaService
    ) {
        this.packageRepository = packageRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.kafkaService = kafkaService;
    }

    @Transactional
    @CacheEvict(value = "packages", allEntries = true)
    public void execute(String packageId) {
        log.info(LOG_ATTEMPT_CANCEL_PACKAGE_TEMPLATE, packageId);

        PackageEntity entity = packageRepository.findById(packageId)
                .orElseThrow(() -> new PackageNotFoundException(packageId));

        if (entity.getStatus() != PackageStatus.CREATED) {
            log.warn(LOG_PACKAGE_CANNOT_BE_CANCELED_TEMPLATE, packageId, entity.getStatus());
            throw new InvalidStatusTransitionException(PACKAGE_CANNOT_BE_CANCELED_MESSAGE);
        }

        entity.setStatus(PackageStatus.CANCELLED);
        entity.setUpdatedAt(LocalDateTime.now());
        packageRepository.save(entity);

        TrackingEventEntity event = TrackingEventEntity.builder()
                .id(UUID.randomUUID())
                .packageEntity(entity)
                .location(TRACKING_EVENT_CANCELLATION_LOCATION)
                .description(TRACKING_EVENT_CANCELLATION_DESCRIPTION)
                .date(LocalDateTime.now())
                .build();

        trackingEventRepository.save(event);

        sendPackageCanceledToKafka(entity.getId());

        log.info(LOG_PACKAGE_CANCELED_SUCCESS_TEMPLATE, packageId);
    }

    private void sendPackageCanceledToKafka(String packageId) {
        String message = String.format(PACKAGE_CANCELED_KAFKA_MESSAGE, packageId);
        kafkaService.sendTrackingEvent(message);
        log.info(LOG_TRACKING_EVENT_SENT_TO_KAFKA_TEMPLATE, packageId, message);
    }

}