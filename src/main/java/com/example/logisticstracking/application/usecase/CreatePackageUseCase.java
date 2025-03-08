package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.PackageCreateRequestDTO;
import com.example.logisticstracking.domain.entity.Package;
import com.example.logisticstracking.domain.enumeration.PackageStatus;
import com.example.logisticstracking.infrastructure.httpclient.resolver.ExternalApisResolver;
import com.example.logisticstracking.infrastructure.mapper.PackageMapper;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreatePackageUseCase {

    private final PackageRepository packageRepository;
    private final ExternalApisResolver externalApisResolver;
    private final PackageMapper packageMapper;

    public CreatePackageUseCase(PackageRepository packageRepository,
                                ExternalApisResolver externalApisResolver,
                                PackageMapper packageMapper) {
        this.packageRepository = packageRepository;
        this.externalApisResolver = externalApisResolver;
        this.packageMapper = packageMapper;
    }

    public Package execute(PackageCreateRequestDTO dto) {
        log.info("Iniciando criação de pacote. sender={}, recipient={}", dto.sender(), dto.recipient());

        String pkgId = (dto.id() != null) ? dto.id() : "pacote-" + UUID.randomUUID();

        LocalDate estimatedDate = parseDate(dto.estimatedDeliveryDate(), LocalDate.now().plusDays(10));

        CompletableFuture<Boolean> isHolidayFuture = externalApisResolver.resolveHoliday(estimatedDate, "BR");
        CompletableFuture<String> dogFactFuture = externalApisResolver.resolveDogFact();

        CompletableFuture.allOf(isHolidayFuture, dogFactFuture).join();

        boolean isHoliday = isHolidayFuture.join();
        String dogFact = dogFactFuture.join();

        Package domainPackage = new Package(
                pkgId,
                dto.description(),
                dto.sender(),
                dto.recipient(),
                PackageStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                isHoliday,
                dogFact,
                estimatedDate.atStartOfDay(),
                List.of()
        );

        PackageEntity entity = packageMapper.toEntity(domainPackage);
        packageRepository.save(entity);

        log.info("Pacote criado com sucesso. ID={}", entity.getId());
        return domainPackage;
    }

    private LocalDate parseDate(String dateStr, LocalDate defaultDate) {
        try {
            if (dateStr != null) {
                String[] parts = dateStr.split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                return LocalDate.of(year, month, day);
            }
        } catch (Exception e) {
            log.warn("Erro ao parsear data={}, usando default={}", dateStr, defaultDate);
        }
        return defaultDate;
    }
}
