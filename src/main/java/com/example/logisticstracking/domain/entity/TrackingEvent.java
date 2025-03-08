package com.example.logisticstracking.domain.entity;

import java.time.LocalDateTime;

public record TrackingEvent(
        Long id,
        String packageId,
        String location,
        String description,
        LocalDateTime date
) {}
