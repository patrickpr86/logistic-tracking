package com.example.logisticstracking.infrastructure.controller;

import com.example.logisticstracking.application.dto.PackageCreateRequestDTO;
import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.application.dto.PackageUpdateStatusRequestDTO;
import com.example.logisticstracking.application.usecase.CancelPackageUseCase;
import com.example.logisticstracking.application.usecase.CreatePackageUseCase;
import com.example.logisticstracking.application.usecase.GetPackageDetailsUseCase;
import com.example.logisticstracking.application.usecase.UpdatePackageStatusUseCase;
import com.example.logisticstracking.domain.entity.Package;
import com.example.logisticstracking.infrastructure.mapper.PackageMapper;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

@Slf4j
@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final CreatePackageUseCase createPackageUseCase;
    private final UpdatePackageStatusUseCase updatePackageStatusUseCase;
    private final CancelPackageUseCase cancelPackageUseCase;
    private final GetPackageDetailsUseCase getPackageDetailsUseCase;
    private final PackageMapper packageMapper; // ✅ Adicionado PackageMapper

    public PackageController(
            CreatePackageUseCase createPackageUseCase,
            UpdatePackageStatusUseCase updatePackageStatusUseCase,
            CancelPackageUseCase cancelPackageUseCase,
            GetPackageDetailsUseCase getPackageDetailsUseCase,
            PackageMapper packageMapper // ✅ Injetando o mapper
    ) {
        this.createPackageUseCase = createPackageUseCase;
        this.updatePackageStatusUseCase = updatePackageStatusUseCase;
        this.cancelPackageUseCase = cancelPackageUseCase;
        this.getPackageDetailsUseCase = getPackageDetailsUseCase;
        this.packageMapper = packageMapper;
    }

    @GetMapping("/{packageId}")
    public ResponseEntity<PackageDetailsDTO> getPackageDetails(
            @PathVariable String packageId,
            @RequestParam(name = "includeEvents", defaultValue = "false") boolean includeEvents
    ) {
        log.info(LOG_GET_PACKAGE_DETAILS_TEMPLATE, packageId, includeEvents);
        PackageDetailsDTO result = getPackageDetailsUseCase.execute(packageId, includeEvents);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<PackageDetailsDTO> createPackage(@RequestBody PackageCreateRequestDTO dto) {
        log.info(LOG_CREATE_PACKAGE_TEMPLATE);
        Package created = createPackageUseCase.execute(dto);
        return ResponseEntity.ok(packageMapper.toDTO(created)); // ✅ Retorna DTO
    }

    @PatchMapping("/{packageId}/status")
    public ResponseEntity<PackageDetailsDTO> updateStatus(
            @PathVariable String packageId,
            @RequestBody PackageUpdateStatusRequestDTO dto
    ) {
        log.info(LOG_UPDATE_PACKAGE_STATUS_TEMPLATE, packageId, dto.status());
        PackageEntity updatedEntity = updatePackageStatusUseCase.execute(packageId, dto);
        Package updatedDomain = packageMapper.toDomain(updatedEntity);
        return ResponseEntity.ok(packageMapper.toDTO(updatedDomain)); // ✅ Retorna DTO
    }

    @PatchMapping("/{packageId}/cancel")
    public ResponseEntity<PackageDetailsDTO> cancelPackage(@PathVariable String packageId) {
        log.info(LOG_CANCEL_PACKAGE_TEMPLATE, packageId);
        PackageEntity cancelledEntity = cancelPackageUseCase.execute(packageId);
        Package cancelledDomain = packageMapper.toDomain(cancelledEntity);
        return ResponseEntity.ok(packageMapper.toDTO(cancelledDomain)); // ✅ Retorna DTO
    }
}
