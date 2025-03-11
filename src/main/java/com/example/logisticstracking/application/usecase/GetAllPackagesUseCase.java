package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.infrastructure.mapper.PackageMapper;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetAllPackagesUseCase {

    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;

    public GetAllPackagesUseCase(PackageRepository packageRepository, PackageMapper packageMapper) {
        this.packageRepository = packageRepository;
        this.packageMapper = packageMapper;
    }

    @Cacheable(value = "allPackages", key = "{#sender, #recipient}")
    public List<PackageDetailsDTO> execute(String sender, String recipient) {
        log.info("Fetching all packages with filters - sender: {}, recipient: {}", sender, recipient);

        FilterType filterType = determineFilterType(sender, recipient);

        List<PackageEntity> packageEntities = switch (filterType) {
            case SENDER_AND_RECIPIENT -> packageRepository.findBySenderAndRecipient(sender, recipient);
            case SENDER_ONLY -> packageRepository.findBySender(sender);
            case RECIPIENT_ONLY -> packageRepository.findByRecipient(recipient);
            case NO_FILTER -> packageRepository.findAll();
        };

        return packageEntities.stream()
                .map(packageMapper::toDomain)
                .map(packageMapper::toDTOWithoutEvents)
                .collect(Collectors.toList());
    }

    private FilterType determineFilterType(String sender, String recipient) {
        if (sender != null && recipient != null) {
            return FilterType.SENDER_AND_RECIPIENT;
        } else if (sender != null) {
            return FilterType.SENDER_ONLY;
        } else if (recipient != null) {
            return FilterType.RECIPIENT_ONLY;
        } else {
            return FilterType.NO_FILTER;
        }
    }

    private enum FilterType {
        SENDER_AND_RECIPIENT, SENDER_ONLY, RECIPIENT_ONLY, NO_FILTER
    }
}
