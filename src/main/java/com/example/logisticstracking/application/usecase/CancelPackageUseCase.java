package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.domain.enumeration.PackageStatus;
import com.example.logisticstracking.domain.exception.InvalidStatusTransitionException;
import com.example.logisticstracking.domain.exception.PackageNotFoundException;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import com.example.logisticstracking.infrastructure.persistence.repository.TrackingEventRepository;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

@Slf4j
@Service
public class CancelPackageUseCase {

    private final PackageRepository packageRepository;
    private final TrackingEventRepository trackingEventRepository;

    public CancelPackageUseCase(PackageRepository packageRepository, TrackingEventRepository trackingEventRepository) {
        this.packageRepository = packageRepository;
        this.trackingEventRepository = trackingEventRepository;
    }

    @Transactional
    public PackageEntity execute(String packageId) {
        log.info(LOG_ATTEMPT_CANCEL_PACKAGE_TEMPLATE, packageId);

        PackageEntity entity = packageRepository.findById(packageId)
                .orElseThrow(() -> new PackageNotFoundException(packageId));

        if (entity.getStatus() != PackageStatus.CREATED) {
            log.warn(LOG_PACKAGE_CANNOT_BE_CANCELED_TEMPLATE, packageId, entity.getStatus());
            throw new InvalidStatusTransitionException(PACKAGE_CANNOT_BE_CANCELED_MESSAGE);
        }

        // Atualiza o status do pacote
        entity.setStatus(PackageStatus.CANCELLED);
        entity.setUpdatedAt(LocalDateTime.now());
        packageRepository.save(entity);

        // Registra um evento de rastreamento informando o cancelamento
        TrackingEventEntity event = TrackingEventEntity.builder()
                .packageEntity(entity)
                .location(TRACKING_EVENT_CANCELLATION_LOCATION)
                .description(TRACKING_EVENT_CANCELLATION_DESCRIPTION)
                .date(LocalDateTime.now())
                .build();

        trackingEventRepository.save(event);

        log.info(LOG_PACKAGE_CANCELED_SUCCESS_TEMPLATE, packageId);
        return entity;
    }
}
