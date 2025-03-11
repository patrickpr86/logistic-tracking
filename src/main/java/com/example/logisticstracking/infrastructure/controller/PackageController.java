package com.example.logisticstracking.infrastructure.controller;

import com.example.logisticstracking.application.dto.PackageCreateRequestDTO;
import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.application.dto.PackageUpdateStatusRequestDTO;
import com.example.logisticstracking.application.usecase.*;
import com.example.logisticstracking.domain.entity.Package;
import com.example.logisticstracking.infrastructure.controller.hateoas.PackageModelAssembler;
import com.example.logisticstracking.infrastructure.mapper.PackageMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/packages")
@Tag(name = "Package API", description = "APIs for managing packages")
public class PackageController {

    private final CreatePackageUseCase createPackageUseCase;
    private final UpdatePackageStatusUseCase updatePackageStatusUseCase;
    private final CancelPackageUseCase cancelPackageUseCase;
    private final GetPackageDetailsUseCase getPackageDetailsUseCase;
    private final GetAllPackagesUseCase getAllPackagesUseCase;
    private final PackageMapper packageMapper;
    private final PackageModelAssembler packageModelAssembler;

    public PackageController(CreatePackageUseCase createPackageUseCase,
                             UpdatePackageStatusUseCase updatePackageStatusUseCase,
                             CancelPackageUseCase cancelPackageUseCase,
                             GetPackageDetailsUseCase getPackageDetailsUseCase,
                             GetAllPackagesUseCase getAllPackagesUseCase,
                             PackageMapper packageMapper,
                             PackageModelAssembler packageModelAssembler) {
        this.createPackageUseCase = createPackageUseCase;
        this.updatePackageStatusUseCase = updatePackageStatusUseCase;
        this.cancelPackageUseCase = cancelPackageUseCase;
        this.getPackageDetailsUseCase = getPackageDetailsUseCase;
        this.getAllPackagesUseCase = getAllPackagesUseCase;
        this.packageMapper = packageMapper;
        this.packageModelAssembler = packageModelAssembler;
    }

    @Operation(
            summary = "Get package details",
            description = "Retrieves detailed information about a package, including its status, sender, recipient, and event history.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Package details retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PackageDetailsDTO.class),
                                    examples = @ExampleObject(value = """
                    {
                        "id": "package-ade01600-3b81-46b0-82d0-061532612902",
                        "description": "PS5",
                        "sender": "Loja ABCD",
                        "recipient": "João Tig",
                        "status": "CANCELLED",
                        "createdAt": "2025-03-08T22:05:00.25204",
                        "updatedAt": "2025-03-08T22:10:52.625983053",
                        "deliveredAt": null,
                        "isHoliday": false,
                        "funFact": "Spiked dog collars were used to protect dogs' throats from wolf attacks in ancient Greece.",
                        "estimatedDeliveryDate": "2025-10-25T00:00:00",
                        "events": [
                            {
                                "id": "8b391697-7a6f-4e56-9424-43a11890b9b2",
                                "packageId": "package-ade01600-3b81-46b0-82d0-061532612902",
                                "location": "System",
                                "description": "Package canceled before shipment",
                                "date": "2025-03-08T22:10:52.62675582"
                            },
                            {
                                "id": "efa540ef-87a0-4705-980c-e6d18737833e",
                                "packageId": "package-ade01600-3b81-46b0-82d0-061532612902",
                                "location": "Origin",
                                "description": "Package created and registered in the system",
                                "date": "2025-03-08T22:05:00.311203"
                            }
                        ]
                    }
                """)
                            ),
                            headers = {
                                    @Header(name = HttpHeaders.CACHE_CONTROL, description = "Defines caching policies for the response."),
                                    @Header(name = HttpHeaders.ETAG, description = "Entity tag used for versioning."),
                                    @Header(name = HttpHeaders.LAST_MODIFIED, description = "Timestamp of the last modification.")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Package not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = """
                    {
                        "path": "/api/packages/package-03dcad1f-68b8-4db7-9170-1bffa6cd1567",
                        "status": 404,
                        "message": "Package not found",
                        "timestamp": "2025-03-08T22:29:15.04175951",
                        "error": "Not Found"
                    }
                """)
                            )
                    )
            }
    )
    @GetMapping(value = "/{packageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDetailsDTO> getPackageDetails(
            @PathVariable String packageId,
            @RequestParam(name = "includeEvents", defaultValue = "false") boolean includeEvents) {
        log.info(LOG_GET_PACKAGE_DETAILS_TEMPLATE, packageId, includeEvents);
        PackageDetailsDTO result = getPackageDetailsUseCase.execute(packageId, includeEvents);

        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(Duration.ofMinutes(5)).getHeaderValue())
                .header(HttpHeaders.ETAG, String.valueOf(result.hashCode()))
                .header(HttpHeaders.LAST_MODIFIED, result.getUpdatedAt().toString())
                .body(packageModelAssembler.toModel(result));
    }


    @Operation(
            summary = "Get all packages",
            description = "Retrieves a list of all packages. Optionally, filters by sender and/or recipient.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of packages retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PackageDetailsDTO.class),
                                    examples = @ExampleObject(value = """
                    {
                        "_embedded": {
                            "packages": [
                                {
                                    "id": "package-ade01600-3b81-46b0-82d0-061532612902",
                                    "description": "PS5",
                                    "sender": "Loja ABCD",
                                    "recipient": "João Tig",
                                    "status": "CANCELLED",
                                    "createdAt": "2025-03-08T22:05:00.25204",
                                    "updatedAt": "2025-03-08T22:10:52.625983053",
                                    "deliveredAt": null
                                },
                                {
                                    "id": "package-ba98f30e-fa94-46aa-90a8-15686d19dea7",
                                    "description": "Laptop",
                                    "sender": "Tech Store",
                                    "recipient": "Maria Silva",
                                    "status": "IN_TRANSIT",
                                    "createdAt": "2025-02-15T10:30:00",
                                    "updatedAt": "2025-02-16T14:45:30",
                                    "deliveredAt": null
                                }
                            ]
                        },
                        "_links": {
                            "self": {
                                "href": "/api/packages"
                            }
                        }
                    }
                """)
                            ),
                            headers = {
                                    @Header(name = HttpHeaders.CACHE_CONTROL, description = "Defines caching policies for the response."),
                                    @Header(name = HttpHeaders.CONTENT_TYPE, description = "Response content type."),
                                    @Header(name = HttpHeaders.LINK, description = "HATEOAS links for navigation.")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = """
                    {
                        "path": "/api/packages",
                        "status": 400,
                        "message": "Invalid sender or recipient parameter",
                        "timestamp": "2025-03-08T22:29:15.04175951",
                        "error": "Bad Request"
                    }
                """)
                            )
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<PackageDetailsDTO>> getAllPackages(
            @RequestParam(required = false) String sender,
            @RequestParam(required = false) String recipient) {
        log.info(LOG_GET_ALL_PACKAGES_TEMPLATE, sender, recipient);
        List<PackageDetailsDTO> packages = getAllPackagesUseCase.execute(sender, recipient).stream()
                .map(packageModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(packages, linkTo(methodOn(PackageController.class).getAllPackages(sender, recipient)).withSelfRel()));
    }


    @Operation(
            summary = "Create a new package",
            description = "Creates a new package with the provided details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the package to be created",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PackageCreateRequestDTO.class),
                            examples = @ExampleObject(value = """
                {
                    "description": "PS5",
                    "sender": "Loja ABCD",
                    "recipient": "João Tig",
                    "estimatedDeliveryDate": "2025-10-25T00:00:00"
                }
            """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Package created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PackageDetailsDTO.class),
                                    examples = @ExampleObject(value = """
                    {
                        "id": "package-ade01600-3b81-46b0-82d0-061532612902",
                        "description": "PS5",
                        "sender": "Loja ABCD",
                        "recipient": "João Tig",
                        "status": "CREATED",
                        "createdAt": "2025-03-08T22:05:00.25204",
                        "updatedAt": "2025-03-08T22:05:00.25204",
                        "deliveredAt": null,
                        "estimatedDeliveryDate": "2025-10-25T00:00:00"
                    }
                """)
                            ),
                            headers = {
                                    @Header(name = HttpHeaders.LOCATION, description = "URI of the created package resource."),
                                    @Header(name = HttpHeaders.CONTENT_TYPE, description = "Response content type.")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = """
                    {
                        "path": "/api/packages",
                        "status": 400,
                        "message": "Invalid input data",
                        "timestamp": "2025-03-08T22:29:15.04175951",
                        "error": "Bad Request"
                    }
                """)
                            )
                    )
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDetailsDTO> createPackage(@Valid @RequestBody PackageCreateRequestDTO dto) {
        log.info(LOG_CREATE_PACKAGE_TEMPLATE);
        Package created = createPackageUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "/api/packages/" + created.id())
                .body(packageModelAssembler.toModel(packageMapper.toDTO(created)));
    }


    @Operation(
            summary = "Update package status",
            description = "Updates the status of an existing package.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Status update request",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PackageUpdateStatusRequestDTO.class),
                            examples = @ExampleObject(value = """
                {
                    "status": "IN_TRANSIT"
                }
            """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Package status updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PackageDetailsDTO.class),
                                    examples = @ExampleObject(value = """
                    {
                        "id": "package-ade01600-3b81-46b0-82d0-061532612902",
                        "description": "PS5",
                        "sender": "Loja ABCD",
                        "recipient": "João Tig",
                        "status": "IN_TRANSIT",
                        "createdAt": "2025-03-08T22:05:00.25204",
                        "updatedAt": "2025-03-08T22:10:52.625983053",
                        "deliveredAt": null
                    }
                """)
                            ),
                            headers = {
                                    @Header(name = HttpHeaders.CACHE_CONTROL, description = "Defines caching policies for the response."),
                                    @Header(name = HttpHeaders.CONTENT_TYPE, description = "Response content type.")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = """
                    {
                        "path": "/api/packages/package-ade01600-3b81-46b0-82d0-061532612902/status",
                        "status": 400,
                        "message": "Invalid status value",
                        "timestamp": "2025-03-08T22:29:15.04175951",
                        "error": "Bad Request"
                    }
                """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Invalid status transition",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = """
                    {
                        "path": "/api/packages/package-03dcad1f-68b8-4db7-9170-1bffa6cd1567/status",
                        "status": 409,
                        "message": "Invalid status transition: IN_TRANSIT -> CANCELLED",
                        "timestamp": "2025-03-08T22:29:15.04175951",
                        "error": "Conflict"
                    }
                """)
                            )
                    )
            }
    )
    @PatchMapping(value = "/{packageId}/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDetailsDTO> updateStatus(@PathVariable String packageId,
                                                          @Valid @RequestBody PackageUpdateStatusRequestDTO dto) {
        log.info(LOG_UPDATE_PACKAGE_STATUS_TEMPLATE, packageId, dto.status());
        Package updated = updatePackageStatusUseCase.execute(packageId, dto);
        return ResponseEntity.ok(packageModelAssembler.toModel(packageMapper.toDTO(updated)));
    }


    @Operation(
            summary = "Cancel a package",
            description = "Cancels an existing package. The cancellation is immediate and cannot be undone.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Package cancelled successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Package not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = """
                    {
                        "path": "/api/packages/package-03dcad1f-68b8-4db7-9170-1bffa6cd1567/cancel",
                        "status": 404,
                        "message": "Package not found",
                        "timestamp": "2025-03-08T22:29:15.04175951",
                        "error": "Not Found"
                    }
                """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Invalid status transition",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = """
                    {
                        "path": "/api/packages/package-03dcad1f-68b8-4db7-9170-1bffa6cd1567/cancel",
                        "status": 409,
                        "message": "Invalid status transition: IN_TRANSIT -> CANCELLED",
                        "timestamp": "2025-03-08T22:29:15.04175951",
                        "error": "Conflict"
                    }
                """)
                            )
                    )
            }
    )
    @PatchMapping("/{packageId}/cancel")
    public ResponseEntity<Void> cancelPackage(@PathVariable String packageId) {
        log.info(LOG_CANCEL_PACKAGE_TEMPLATE, packageId);
        cancelPackageUseCase.execute(packageId);
        return ResponseEntity.noContent().build();
    }

}
