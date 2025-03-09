package com.example.logisticstracking.application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record TrackingEventDTO(
        UUID id,
        String packageId,
        String location,
        String description,
        LocalDateTime date
) implements Serializable {
}
