package com.example.logisticstracking.application.dto;

public record TrackingEventRequestDTO(
        String packageId,
        String location,
        String description,
        String date
) {}
