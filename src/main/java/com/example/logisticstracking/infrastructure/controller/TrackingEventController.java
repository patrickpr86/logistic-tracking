package com.example.logisticstracking.infrastructure.controller;

import com.example.logisticstracking.application.dto.TrackingEventRequestDTO;

import com.example.logisticstracking.application.usecase.CreateTrackingEventUseCase;
import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tracking-events")
public class TrackingEventController {

    private final CreateTrackingEventUseCase createTrackingEventUseCase;

    public TrackingEventController(CreateTrackingEventUseCase createTrackingEventUseCase) {
        this.createTrackingEventUseCase = createTrackingEventUseCase;
    }

    @PostMapping
    public ResponseEntity<TrackingEventEntity> createEvent(@RequestBody TrackingEventRequestDTO dto) {
        log.info("Recebida requisição para criar TrackingEvent: {}", dto);
        return ResponseEntity.ok(createTrackingEventUseCase.execute(dto));
    }
}
