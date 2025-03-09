package com.example.logisticstracking.application.dto;

import java.io.Serializable;

public record PackageCreateRequestDTO(
        String id,
        String description,
        String sender,
        String recipient,
        String estimatedDeliveryDate
) implements Serializable {
}
