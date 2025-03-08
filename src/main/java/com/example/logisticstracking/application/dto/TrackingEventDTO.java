package com.example.logisticstracking.application.dto;

import java.time.LocalDateTime;

public record TrackingEventDTO(
        Long id,
        String packageId,
        String location,
        String description,
        LocalDateTime date
) {}
