package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.domain.enumeration.PackageStatus;
import com.example.logisticstracking.domain.exception.InvalidStatusTransitionException;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CancelPackageUseCase {

    private final PackageRepository packageRepository;

    public CancelPackageUseCase(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public PackageEntity execute(String packageId) {
        log.info("Tentando cancelar pacote: {}", packageId);

        PackageEntity entity = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Pacote não encontrado: " + packageId));

        if (entity.getStatus() == PackageStatus.CREATED) {
            entity.setStatus(PackageStatus.CANCELLED);
            entity.setUpdatedAt(LocalDateTime.now());
            packageRepository.save(entity);

            log.info("Pacote={} cancelado com sucesso.", packageId);
            return entity;
        } else {
            log.warn("Não é possível cancelar pacote={}, status atual={}", packageId, entity.getStatus());
            throw new InvalidStatusTransitionException(
                    "Não é possível cancelar o pacote pois não está em CREATED."
            );
        }
    }
}
