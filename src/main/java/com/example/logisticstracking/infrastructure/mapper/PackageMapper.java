package com.example.logisticstracking.infrastructure.mapper;

import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.application.dto.TrackingEventDTO;
import com.example.logisticstracking.domain.entity.Package;
import com.example.logisticstracking.domain.entity.TrackingEvent;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

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
                .trackingEvents(new ArrayList<>()) // ✅ lista inicializada corretamente
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
        return new PackageDetailsDTO(
                domain.id(),
                domain.description(),
                domain.sender(),
                domain.recipient(),
                domain.status(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deliveredAt(),
                domain.isHoliday(),
                domain.funFact(),
                domain.estimatedDeliveryDate(),
                mapTrackingEventDTOs(domain.events())
        );
    }

    public PackageDetailsDTO toDetailsDTO(PackageEntity entity, List<TrackingEventDTO> events) {
        return new PackageDetailsDTO(
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
                events
        );
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
                        event.id(),
                        event.packageId(),
                        event.location(),
                        event.description(),
                        event.date()
                ))
                .collect(Collectors.toList());
    }
}
