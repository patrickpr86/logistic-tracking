package com.example.logisticstracking.application.dto;

import com.example.logisticstracking.domain.enumeration.PackageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PackageCreateRequestDTO(
        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Sender is required")
        String sender,

        @NotBlank(message = "Recipient is required")
        String recipient,

        @NotNull(message = "Estimated delivery date is required")
        String estimatedDeliveryDate
) {
}
