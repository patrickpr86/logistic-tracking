package com.example.logisticstracking.application.dto;

public record PackageCreateRequestDTO(
        String id,
        String description,
        String sender,
        String recipient,
        String estimatedDeliveryDate
) {}
