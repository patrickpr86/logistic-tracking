package com.example.logisticstracking.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracking_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String packageId;
    private String location;
    private String description;
    private LocalDateTime date;
}
