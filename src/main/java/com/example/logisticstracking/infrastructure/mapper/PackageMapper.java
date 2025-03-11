package com.example.logisticstracking.infrastructure.mapper;

import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.application.dto.TrackingEventDTO;
import com.example.logisticstracking.domain.entity.Package;
import com.example.logisticstracking.domain.entity.TrackingEvent;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PackageMapper {

    public PackageEntity toEntity(Package domain) {
        return PackageEntity.builder()
                .id(domain.id())
                .description(domain.description())
                .sender(domain.sender())
                .recipient(domain.recipient())
                .status(domain.status())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .deliveredAt(domain.deliveredAt())
                .isHoliday(domain.isHoliday())
                .funFact(domain.funFact())
                .estimatedDeliveryDate(domain.estimatedDeliveryDate())
                .trackingEvents(new ArrayList<>())
                .build();
    }

    public Package toDomain(PackageEntity entity) {
        return new Package(
                entity.getId(),
                entity.getDescription(),
                entity.getSender(),
                entity.getRecipient(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeliveredAt(),
                entity.isHoliday(),
                entity.getFunFact(),
                entity.getEstimatedDeliveryDate(),
                mapTrackingEvents(entity.getTrackingEvents())
        );
    }

    public PackageDetailsDTO toDTO(Package domain) {
        return PackageDetailsDTO.builder()
                .id(domain.id())
                .description(domain.description())
                .sender(domain.sender())
                .recipient(domain.recipient())
                .status(domain.status().name())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .estimatedDeliveryDate(domain.estimatedDeliveryDate())
                .trackingEvents(mapTrackingEventDTOs(domain.events()))
                .build();
    }

    public PackageDetailsDTO toDTOWithoutEvents(Package domain) {
        return PackageDetailsDTO.builder()
                .id(domain.id())
                .description(domain.description())
                .sender(domain.sender())
                .recipient(domain.recipient())
                .status(domain.status().name())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .estimatedDeliveryDate(domain.estimatedDeliveryDate())
                .build();
    }


    public PackageDetailsDTO toDetailsDTO(PackageEntity entity, List<TrackingEventDTO> events) {
        return PackageDetailsDTO.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .sender(entity.getSender())
                .recipient(entity.getRecipient())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .estimatedDeliveryDate(entity.getEstimatedDeliveryDate())
                .trackingEvents(events)
                .build();
    }

    private List<TrackingEvent> mapTrackingEvents(List<TrackingEventEntity> entities) {
        if (entities == null) return new ArrayList<>();

        return entities.stream()
                .map(entity -> new TrackingEvent(
                        entity.getId(),
                        entity.getPackageEntity().getId(),
                        entity.getLocation(),
                        entity.getDescription(),
                        entity.getDate()
                ))
                .collect(Collectors.toList());
    }

    private List<TrackingEventDTO> mapTrackingEventDTOs(List<TrackingEvent> trackingEvents) {
        if (trackingEvents == null) return new ArrayList<>();

        return trackingEvents.stream()
                .map(event -> new TrackingEventDTO(
                        event.packageId(),
                        event.location(),
                        event.description(),
                        event.date()
                ))
                .collect(Collectors.toList());
    }
}
