package com.example.logisticstracking.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record TrackingEvent(
        UUID id,
        String packageId,
        String location,
        String description,
        LocalDateTime date
) implements Serializable {
}
