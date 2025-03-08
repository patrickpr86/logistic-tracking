package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.PackageUpdateStatusRequestDTO;
import com.example.logisticstracking.domain.enumeration.PackageStatus;
import com.example.logisticstracking.domain.exception.InvalidStatusTransitionException;

import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UpdatePackageStatusUseCase {

    private final PackageRepository packageRepository;

    public UpdatePackageStatusUseCase(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public PackageEntity execute(String packageId, PackageUpdateStatusRequestDTO dto) {
        log.info("Atualizando status do pacote={} para {}", packageId, dto.status());

        PackageEntity entity = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Pacote não encontrado: " + packageId));

        PackageStatus newStatus = PackageStatus.valueOf(String.valueOf(dto.status()));

        if (!isValidTransition(entity.getStatus(), newStatus)) {
            throw new InvalidStatusTransitionException("Transição inválida: " + entity.getStatus() + " -> " + newStatus);
        }

        entity.setStatus(newStatus);
        entity.setUpdatedAt(LocalDateTime.now());
        if (newStatus == PackageStatus.DELIVERED) {
            entity.setDeliveredAt(LocalDateTime.now());
        }
        packageRepository.save(entity);

        log.info("Status do pacote={} atualizado para {}", packageId, newStatus);
        return entity;
    }

    private boolean isValidTransition(PackageStatus current, PackageStatus next) {
        return switch (current) {
            case CREATED -> next == PackageStatus.IN_TRANSIT
                    || next == PackageStatus.CANCELLED
                    || next == PackageStatus.CREATED;
            case IN_TRANSIT -> next == PackageStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }
}

