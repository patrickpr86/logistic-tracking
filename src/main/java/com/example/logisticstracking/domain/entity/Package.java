package com.example.logisticstracking.domain.entity;

import com.example.logisticstracking.domain.enumeration.PackageStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record Package(
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
        List<TrackingEvent> events
) {
    public Package {
        events = events != null ? List.copyOf(events) : List.of();
    }

    public Package addEvent(TrackingEvent event) {
        List<TrackingEvent> updatedEvents = new ArrayList<>(this.events);
        updatedEvents.add(event);
        return new Package(
                id, description, sender, recipient, status, createdAt, updatedAt,
                deliveredAt, isHoliday, funFact, estimatedDeliveryDate, updatedEvents
        );
    }
}
