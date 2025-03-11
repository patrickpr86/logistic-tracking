package com.example.logisticstracking.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TrackingEventDTO(
        @NotBlank(message = "Package ID is required")
        String packageId,

        @NotBlank(message = "Location is required")
        String location,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Date is required")
        LocalDateTime date
) {
}

