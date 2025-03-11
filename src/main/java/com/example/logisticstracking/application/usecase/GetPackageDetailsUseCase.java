package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.application.dto.TrackingEventDTO;
import com.example.logisticstracking.domain.exception.PackageNotFoundException;
import com.example.logisticstracking.infrastructure.mapper.PackageMapper;
import com.example.logisticstracking.infrastructure.mapper.TrackingEventMapper;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import com.example.logisticstracking.infrastructure.persistence.repository.TrackingEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.logisticstracking.domain.constants.PackageConstants.LOG_FETCH_PACKAGE_DETAILS_TEMPLATE;
import static com.example.logisticstracking.domain.constants.PackageConstants.LOG_PACKAGE_DETAILS_FETCHED_TEMPLATE;

/**
 * Use case for retrieving package details.
 * <p>
 * This service fetches package details and, optionally, includes tracking events.
 * It is optimized with caching for better performance.
 * </p>
 *
 * @author Patrick Pascoal Ribeiro
 */
@Slf4j
@Service
public class GetPackageDetailsUseCase {

    private final PackageRepository packageRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final PackageMapper packageMapper;
    private final TrackingEventMapper trackingEventMapper;

    public GetPackageDetailsUseCase(PackageRepository packageRepository,
                                    TrackingEventRepository trackingEventRepository,
                                    PackageMapper packageMapper,
                                    TrackingEventMapper trackingEventMapper) {
        this.packageRepository = packageRepository;
        this.trackingEventRepository = trackingEventRepository;
        this.packageMapper = packageMapper;
        this.trackingEventMapper = trackingEventMapper;
    }

    /**
     * Retrieves package details.
     * <p>
     * If `includeEvents` is true, tracking events are fetched and included in the response.
     * Otherwise, an empty list is returned.
     * </p>
     *
     * @param packageId    The ID of the package.
     * @param includeEvents Whether to include tracking events in the response.
     * @return PackageDetailsDTO containing package information and optional tracking events.
     * @throws PackageNotFoundException if the package is not found.
     */
    @Cacheable(value = "packages", key = "#packageId")
    public PackageDetailsDTO execute(String packageId, boolean includeEvents) {

        log.info(LOG_FETCH_PACKAGE_DETAILS_TEMPLATE, packageId, includeEvents);

        PackageEntity packageEntity = packageRepository.findById(packageId)
                .orElseThrow(() -> new PackageNotFoundException(packageId));

        List<TrackingEventDTO> trackingEvents = includeEvents
                ? trackingEventRepository.findByPackageEntityIdOrderByDateAsc(packageId).stream()
                .map(trackingEventMapper::toDTO)
                .toList()
                : List.of();

        PackageDetailsDTO packageDetails = packageMapper.toDetailsDTO(packageEntity, trackingEvents);

        log.info(LOG_PACKAGE_DETAILS_FETCHED_TEMPLATE, packageId);
        return packageDetails;
    }
}
