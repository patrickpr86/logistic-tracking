package com.example.logisticstracking.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public record TrackingEventRequestDTO(
        @NotBlank(message = "Package ID cannot be blank")
        String packageId,

        @NotBlank(message = "Location cannot be blank")
        String location,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Date cannot be null")
        LocalDateTime date
) implements Serializable {
}
