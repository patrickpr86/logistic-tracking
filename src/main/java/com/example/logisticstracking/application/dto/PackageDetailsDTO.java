package com.example.logisticstracking.application.dto;

import com.example.logisticstracking.domain.enumeration.PackageStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record PackageDetailsDTO(
        String id,
        String description,
        String sender,
        String recipient,
        PackageStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deliveredAt,
        boolean isHoliday,
        String funFact,
        LocalDateTime estimatedDeliveryDate,
        List<TrackingEventDTO> events
) implements Serializable {
}
