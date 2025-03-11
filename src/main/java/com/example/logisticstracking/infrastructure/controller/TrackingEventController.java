package com.example.logisticstracking.infrastructure.controller;

import com.example.logisticstracking.application.dto.TrackingEventDTO;
import com.example.logisticstracking.application.dto.TrackingEventRequestDTO;
import com.example.logisticstracking.application.usecase.CreateTrackingEventUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.logisticstracking.domain.constants.PackageConstants.LOG_RECEIVED_TRACKING_EVENT_REQUEST_TEMPLATE;

@Slf4j
@RestController
@RequestMapping("/api/tracking-events")
@Tag(name = "Tracking Events API", description = "API for managing package tracking events")
public class TrackingEventController {

    private final CreateTrackingEventUseCase createTrackingEventUseCase;

    public TrackingEventController(CreateTrackingEventUseCase createTrackingEventUseCase) {
        this.createTrackingEventUseCase = createTrackingEventUseCase;
    }

    @Operation(
            summary = "Register a new tracking event",
            description = "Receives a tracking event and processes it asynchronously using Kafka.",
            requestBody = @RequestBody(
                    description = "Tracking event payload",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TrackingEventRequestDTO.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "packageId": "package-12345",
                                          "location": "Distribution Center - São Paulo",
                                          "description": "Package arrived at distribution center",
                                          "date": "2025-01-20T11:00:00Z"
                                        }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Tracking event successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TrackingEventDTO.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "packageId": "package-12345",
                                          "location": "Distribution Center - São Paulo",
                                          "description": "Package arrived at distribution center",
                                          "date": "2025-01-20T11:00:00Z"
                                        }
                                    """)
                    ),
                    headers = {
                            @Header(name = HttpHeaders.CACHE_CONTROL, description = "Cache control directive", schema = @Schema(type = "string")),
                            @Header(name = HttpHeaders.CONTENT_TYPE, description = "Response content type (application/json)", schema = @Schema(type = "string"))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in request fields",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                        {
                                          "timestamp": "2025-03-10T23:31:28.091+00:00",
                                          "status": 400,
                                          "error": "Validation Error",
                                          "path": "/api/tracking-events",
                                          "errors": {
                                            "packageId": "Package ID cannot be blank",
                                            "date": "Date cannot be null"
                                          }
                                        }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Package not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                        {
                                          "timestamp": "2025-03-10T23:31:28.091+00:00",
                                          "status": 404,
                                          "error": "Package not found",
                                          "message": "Package ID package-12345 not found",
                                          "path": "/api/tracking-events"
                                        }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                        {
                                          "timestamp": "2025-03-10T23:31:28.091+00:00",
                                          "status": 500,
                                          "error": "Internal Server Error",
                                          "message": "Unexpected error occurred",
                                          "path": "/api/tracking-events"
                                        }
                                    """)
                    )
            )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrackingEventDTO> createEvent(
            @Valid @RequestBody TrackingEventRequestDTO dto) {
        log.info(LOG_RECEIVED_TRACKING_EVENT_REQUEST_TEMPLATE, dto);
        TrackingEventDTO response = createTrackingEventUseCase.execute(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.CACHE_CONTROL, CacheControl.noStore().getHeaderValue())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
