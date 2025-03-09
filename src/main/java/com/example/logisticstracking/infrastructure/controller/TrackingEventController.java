package com.example.logisticstracking.infrastructure.controller;

import com.example.logisticstracking.application.dto.TrackingEventDTO;
import com.example.logisticstracking.application.dto.TrackingEventRequestDTO;
import com.example.logisticstracking.application.usecase.CreateTrackingEventUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.logisticstracking.domain.constants.PackageConstants.LOG_RECEIVED_TRACKING_EVENT_REQUEST_TEMPLATE;

@Slf4j
@RestController
@RequestMapping("/api/tracking-events")
public class TrackingEventController {

    private final CreateTrackingEventUseCase createTrackingEventUseCase;

    public TrackingEventController(CreateTrackingEventUseCase createTrackingEventUseCase) {
        this.createTrackingEventUseCase = createTrackingEventUseCase;
    }

    @PostMapping
    public ResponseEntity<TrackingEventDTO> createEvent(@RequestBody TrackingEventRequestDTO dto) {
        log.info(LOG_RECEIVED_TRACKING_EVENT_REQUEST_TEMPLATE, dto);
        TrackingEventDTO response = createTrackingEventUseCase.execute(dto);
        return ResponseEntity.ok(response);
    }
}