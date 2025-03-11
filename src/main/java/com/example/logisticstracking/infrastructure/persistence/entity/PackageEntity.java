package com.example.logisticstracking.infrastructure.persistence.entity;

import com.example.logisticstracking.domain.enumeration.PackageStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "packages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageEntity {

    @Id
    @Column(length = 50, nullable = false, updatable = false)
    private String id;

    private String description;
    private String sender;
    private String recipient;

    @Enumerated(EnumType.STRING)
    private PackageStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveredAt;

    private boolean isHoliday;

    @Column(length = 1024)
    private String funFact;
    private LocalDateTime estimatedDeliveryDate;

    @OneToMany(mappedBy = "packageEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("packageEntity")
    private List<TrackingEventEntity> trackingEvents = new ArrayList<>();
}

