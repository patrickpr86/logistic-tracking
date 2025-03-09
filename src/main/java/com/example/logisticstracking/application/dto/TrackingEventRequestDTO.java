package com.example.logisticstracking.application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record TrackingEventRequestDTO(
        String packageId,
        String location,
        String description,
        LocalDateTime date
) implements Serializable {
}
