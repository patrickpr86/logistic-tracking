package com.example.logisticstracking.infrastructure.mapper;

import com.example.logisticstracking.application.dto.TrackingEventDTO;
import com.example.logisticstracking.domain.entity.TrackingEvent;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TrackingEventMapper {


    public TrackingEventDTO toDTO(TrackingEventEntity entity) {
        return new TrackingEventDTO(
                entity.getPackageEntity().getId(),
                entity.getLocation(),
                entity.getDescription(),
                entity.getDate()
        );
    }



    public List<TrackingEvent> toTrackingEventList(List<TrackingEventEntity> entities) {
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


}
