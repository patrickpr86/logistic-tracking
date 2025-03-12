package com.example.logisticstracking.infrastructure.kafka.dto;

import com.example.logisticstracking.domain.enumeration.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEventKafkaDTO {
    private PackageStatus status;
    private String trackingId;
    private String details;
}
