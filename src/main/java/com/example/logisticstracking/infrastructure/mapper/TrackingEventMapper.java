package com.example.logisticstracking.infrastructure.mapper;

import com.example.logisticstracking.domain.entity.TrackingEvent;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import org.springframework.stereotype.Component;

@Component
public class TrackingEventMapper {


    public TrackingEventEntity toEntity(TrackingEvent domain) {
        if (domain == null) {
            return null;
        }
        return TrackingEventEntity.builder()
                .id(domain.id())
                .packageId(domain.packageId())
                .location(domain.location())
                .description(domain.description())
                .date(domain.date())
                .build();
    }

    public TrackingEvent toDomain(TrackingEventEntity entity) {
        if (entity == null) {
            return null;
        }
        return new TrackingEvent(
                entity.getId(),
                entity.getPackageId(),
                entity.getLocation(),
                entity.getDescription(),
                entity.getDate()
        );
    }
}
