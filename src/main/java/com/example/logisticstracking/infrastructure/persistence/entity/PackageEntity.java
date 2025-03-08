package com.example.logisticstracking.infrastructure.persistence.entity;

import com.example.logisticstracking.domain.enumeration.PackageStatus;
import jakarta.persistence.*;


import java.time.LocalDateTime;
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
    @Column(nullable = false)
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
    private String funFact;
    private LocalDateTime estimatedDeliveryDate;
}
