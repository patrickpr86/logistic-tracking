package com.example.logisticstracking.infrastructure.controller;

import com.example.logisticstracking.application.dto.PackageCreateRequestDTO;
import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.application.dto.PackageUpdateStatusRequestDTO;
import com.example.logisticstracking.application.usecase.CancelPackageUseCase;
import com.example.logisticstracking.application.usecase.CreatePackageUseCase;
import com.example.logisticstracking.application.usecase.GetPackageDetailsUseCase;
import com.example.logisticstracking.application.usecase.UpdatePackageStatusUseCase;
import com.example.logisticstracking.domain.entity.Package;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final CreatePackageUseCase createPackageUseCase;
    private final UpdatePackageStatusUseCase updatePackageStatusUseCase;
    private final CancelPackageUseCase cancelPackageUseCase;
    private final GetPackageDetailsUseCase getPackageDetailsUseCase;

    public PackageController(CreatePackageUseCase createPackageUseCase,
                             UpdatePackageStatusUseCase updatePackageStatusUseCase, CancelPackageUseCase cancelPackageUseCase, GetPackageDetailsUseCase getPackageDetailsUseCase) {
        this.createPackageUseCase = createPackageUseCase;
        this.updatePackageStatusUseCase = updatePackageStatusUseCase;
        this.cancelPackageUseCase = cancelPackageUseCase;
        this.getPackageDetailsUseCase = getPackageDetailsUseCase;
    }

    @GetMapping("/{packageId}")
    public ResponseEntity<PackageDetailsDTO> getPackageDetails(
            @PathVariable String packageId,
            @RequestParam(name = "includeEvents", defaultValue = "false") boolean includeEvents
    ) {
        log.info("GET /api/packages/{}?includeEvents={}", packageId, includeEvents);
        PackageDetailsDTO result = getPackageDetailsUseCase.execute(packageId, includeEvents);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Package> createPackage(@RequestBody PackageCreateRequestDTO dto) {
        log.info("POST /api/packages => Criar pacote");
        Package created = createPackageUseCase.execute(dto);
        return ResponseEntity.ok(created);
    }

    @PatchMapping("/{packageId}/status")
    public ResponseEntity<PackageEntity> updateStatus(
            @PathVariable String packageId,
            @RequestBody PackageUpdateStatusRequestDTO dto
    ) {
        log.info("PATCH /api/packages/{}/status => Atualizar status p/ {}", packageId, dto.status());
        PackageEntity updated = updatePackageStatusUseCase.execute(packageId, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{packageId}/cancel")
    public ResponseEntity<PackageEntity> cancelPackage(@PathVariable String packageId) {
        log.info("PATCH /api/packages/{}/cancel => Cancelar pacote", packageId);
        PackageEntity cancelled = cancelPackageUseCase.execute(packageId);
        return ResponseEntity.ok(cancelled);
    }
}
