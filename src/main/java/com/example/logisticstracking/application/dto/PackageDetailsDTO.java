package com.example.logisticstracking.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageDetailsDTO extends RepresentationModel<PackageDetailsDTO> {
    private String id;
    private String description;
    private String sender;
    private String recipient;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime estimatedDeliveryDate;
    private List<TrackingEventDTO> trackingEvents;
}
