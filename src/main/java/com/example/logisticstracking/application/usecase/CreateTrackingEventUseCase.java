package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.TrackingEventDTO;
import com.example.logisticstracking.application.dto.TrackingEventRequestDTO;
import com.example.logisticstracking.domain.exception.PackageNotFoundException;
import com.example.logisticstracking.infrastructure.kafka.service.KafkaService;
import com.example.logisticstracking.infrastructure.mapper.TrackingEventMapper;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import com.example.logisticstracking.infrastructure.persistence.repository.TrackingEventRepository;
import com.example.logisticstracking.infrastructure.utils.DateUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

@Slf4j
@Service
public class CreateTrackingEventUseCase {

    private final PackageRepository packageRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final TrackingEventMapper trackingEventMapper;
    private final KafkaService kafkaService;

    public CreateTrackingEventUseCase(
            PackageRepository packageRepository,
            TrackingEventRepository trackingEventRepository,
            TrackingEventMapper trackingEventMapper,
            KafkaService kafkaService
    ) {
        this.packageRepository = packageRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.trackingEventMapper = trackingEventMapper;
        this.kafkaService = kafkaService;
    }

    @Transactional
    public TrackingEventDTO execute(TrackingEventRequestDTO dto) {
        log.info(LOG_CREATING_TRACKING_EVENT_TEMPLATE, dto.packageId());

        PackageEntity packageEntity = packageRepository.findById(dto.packageId())
                .orElseThrow(() -> new PackageNotFoundException(dto.packageId()));

        LocalDateTime eventDate = DateUtils.parseDateTime(dto.date().toString(), LocalDateTime.now());

        TrackingEventEntity trackingEventEntity = TrackingEventEntity.builder()
                .id(UUID.randomUUID())
                .packageEntity(packageEntity)
                .location(dto.location())
                .description(dto.description())
                .date(eventDate)
                .build();

        TrackingEventEntity savedEvent = trackingEventRepository.save(trackingEventEntity);


        sendTrackingEventToKafka(packageEntity.getId(), savedEvent.getDescription());

        log.info(LOG_TRACKING_EVENT_CREATED_SUCCESS_TEMPLATE, savedEvent.getId(), packageEntity.getId());

        return trackingEventMapper.toDTO(savedEvent);
    }

    private void sendTrackingEventToKafka(String packageId, String description) {
        String message = String.format(TRACKING_EVENT_MESSAGE, packageId, description);
        kafkaService.sendTrackingEvent(message);
        log.info(LOG_TRACKING_EVENT_SENT_TO_KAFKA_TEMPLATE, packageId, message);
    }
}
