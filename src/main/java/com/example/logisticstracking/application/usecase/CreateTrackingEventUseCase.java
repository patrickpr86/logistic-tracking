package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.TrackingEventRequestDTO;
import com.example.logisticstracking.domain.entity.TrackingEvent;
import com.example.logisticstracking.infrastructure.kafka.service.KafkaService;
import com.example.logisticstracking.infrastructure.mapper.TrackingEventMapper;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import com.example.logisticstracking.infrastructure.persistence.repository.TrackingEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public TrackingEventEntity execute(TrackingEventRequestDTO dto) {
        log.info("Criando tracking event para pacote={}", dto.packageId());

        PackageEntity pkg = packageRepository.findById(dto.packageId())
                //TODO CRIAR EXCEPTION PERSONALIZADA
                .orElseThrow(() -> new RuntimeException("Pacote não encontrado: " + dto.packageId()));

        LocalDateTime eventDate = parseDate(dto.date());

        TrackingEvent domain = new TrackingEvent(
                null, // ID será gerado pelo banco
                pkg.getId(),
                dto.location(),
                dto.description(),
                eventDate
        );

        TrackingEventEntity entity = trackingEventMapper.toEntity(domain);

        TrackingEventEntity saved = trackingEventRepository.save(entity);

        String message = String.format("Evento criado para pacote %s: %s", pkg.getId(), domain.description());
        kafkaService.sendTrackingEvent(message);

        log.info("TrackingEvent={} criado com sucesso para pacote={}", saved.getId(), pkg.getId());
        return saved;
    }

    private LocalDateTime parseDate(String dateStr) {
        if (dateStr != null && !dateStr.isBlank()) {
            try {
                return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
            } catch (Exception e) {
                log.warn("Erro ao parsear data={}, usando timestamp atual", dateStr);
            }
        }
        return LocalDateTime.now();
    }
}