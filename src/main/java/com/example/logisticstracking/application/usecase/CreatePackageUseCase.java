package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.PackageCreateRequestDTO;
import com.example.logisticstracking.domain.entity.Package;
import com.example.logisticstracking.domain.enumeration.PackageStatus;
import com.example.logisticstracking.infrastructure.httpclient.resolver.ExternalApisResolver;
import com.example.logisticstracking.infrastructure.kafka.service.KafkaService;
import com.example.logisticstracking.infrastructure.mapper.PackageMapper;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import com.example.logisticstracking.infrastructure.persistence.repository.TrackingEventRepository;
import com.example.logisticstracking.infrastructure.utils.DateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

@Slf4j
@Service
public class CreatePackageUseCase {

    private final PackageRepository packageRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final ExternalApisResolver externalApisResolver;
    private final PackageMapper packageMapper;
    private final KafkaService kafkaService;

    public CreatePackageUseCase(
            PackageRepository packageRepository,
            TrackingEventRepository trackingEventRepository,
            ExternalApisResolver externalApisResolver,
            PackageMapper packageMapper,
            KafkaService kafkaService
    ) {
        this.packageRepository = packageRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.externalApisResolver = externalApisResolver;
        this.packageMapper = packageMapper;
        this.kafkaService = kafkaService;
    }

    @Transactional
    @CacheEvict(value = "packages", allEntries = true)
    public Package execute(PackageCreateRequestDTO dto) {
        log.info(LOG_START_PACKAGE_CREATION_TEMPLATE, dto.sender(), dto.recipient());

        String pkgId = (dto.id() != null) ? dto.id() : PACKAGE_PREFIX_ID + UUID.randomUUID();

        LocalDate estimatedDate = DateUtils.parseDate(
                dto.estimatedDeliveryDate(),
                LocalDate.now().plusDays(DEFAULT_ESTIMATED_DAYS)
        );

        CompletableFuture<Boolean> isHolidayFuture = externalApisResolver.resolveHoliday(estimatedDate, DEFAULT_COUNTRY);
        CompletableFuture<String> dogFactFuture = externalApisResolver.resolveDogFact();

        CompletableFuture.allOf(isHolidayFuture, dogFactFuture).join();

        boolean isHoliday = isHolidayFuture.join();
        String dogFact = dogFactFuture.join();

        Package domainPackage = new Package(
                pkgId,
                dto.description(),
                dto.sender(),
                dto.recipient(),
                PackageStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                isHoliday,
                dogFact,
                estimatedDate.atStartOfDay(),
                new ArrayList<>()
        );

        PackageEntity entity = packageMapper.toEntity(domainPackage);
        packageRepository.save(entity);

        TrackingEventEntity event = TrackingEventEntity.builder()
                .id(UUID.randomUUID())
                .packageEntity(entity)
                .location(PACKAGE_ORIGIN_LOCATION)
                .description(PACKAGE_CREATED_DESCRIPTION)
                .date(LocalDateTime.now())
                .build();


        entity.getTrackingEvents().add(event);
        packageRepository.save(entity);

        sendPackageCreatedToKafka(entity.getId());

        log.info(LOG_PACKAGE_CREATED_SUCCESS_TEMPLATE, entity.getId());

        return packageMapper.toDomain(entity);
    }

    private void sendPackageCreatedToKafka(String packageId) {
        String message = String.format("Package %s created successfully", packageId);
        kafkaService.sendTrackingEvent(message);
        log.info("Sent package creation event to Kafka: {}", message);
    }
}
