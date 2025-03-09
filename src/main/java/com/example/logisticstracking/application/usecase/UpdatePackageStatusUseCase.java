package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.PackageUpdateStatusRequestDTO;
import com.example.logisticstracking.domain.enumeration.PackageStatus;
import com.example.logisticstracking.domain.exception.InvalidStatusTransitionException;
import com.example.logisticstracking.domain.exception.PackageNotFoundException;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import com.example.logisticstracking.infrastructure.persistence.repository.TrackingEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

@Slf4j
@Service
public class UpdatePackageStatusUseCase {

    private final PackageRepository packageRepository;
    private final TrackingEventRepository trackingEventRepository;

    public UpdatePackageStatusUseCase(PackageRepository packageRepository, TrackingEventRepository trackingEventRepository) {
        this.packageRepository = packageRepository;
        this.trackingEventRepository = trackingEventRepository;
    }
    @CacheEvict(value = "packages", allEntries = true)
    @Transactional
    public PackageEntity execute(String packageId, PackageUpdateStatusRequestDTO dto) {
        log.info(LOG_UPDATING_PACKAGE_STATUS_TEMPLATE, packageId, dto.status());

        PackageEntity entity = packageRepository.findById(packageId)
                .orElseThrow(() -> new PackageNotFoundException(packageId));

        PackageStatus newStatus = PackageStatus.valueOf(dto.status().name());

        if (!isValidTransition(entity.getStatus(), newStatus)) {
            throw new InvalidStatusTransitionException(
                    String.format(INVALID_STATUS_TRANSITION_MESSAGE, entity.getStatus(), newStatus)
            );
        }

        entity.setStatus(newStatus);
        entity.setUpdatedAt(LocalDateTime.now());

        if (newStatus == PackageStatus.DELIVERED) {
            entity.setDeliveredAt(LocalDateTime.now());
        }

        packageRepository.save(entity);

        registerTrackingEvent(entity, newStatus);

        log.info(LOG_PACKAGE_STATUS_UPDATED_TEMPLATE, packageId, newStatus);
        return entity;
    }

    private boolean isValidTransition(PackageStatus current, PackageStatus next) {
        return switch (current) {
            case CREATED -> next == PackageStatus.IN_TRANSIT
                    || next == PackageStatus.CANCELLED
                    || next == PackageStatus.CREATED;
            case IN_TRANSIT -> next == PackageStatus.DELIVERED;
            case DELIVERED, CANCELLED, UNKNOWN-> false;
        };
    }

    private void registerTrackingEvent(PackageEntity packageEntity, PackageStatus newStatus) {
        TrackingEventEntity trackingEvent = TrackingEventEntity.builder()
                .packageEntity(packageEntity)
                .location(TRACKING_EVENT_CANCELLATION_LOCATION)
                .description(PACKAGE_CREATED_DESCRIPTION + newStatus)
                .date(LocalDateTime.now())
                .build();

        trackingEventRepository.save(trackingEvent);
    }
}
