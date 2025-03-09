package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.application.dto.TrackingEventDTO;
import com.example.logisticstracking.domain.exception.PackageNotFoundException;
import com.example.logisticstracking.infrastructure.mapper.PackageMapper;
import com.example.logisticstracking.infrastructure.mapper.TrackingEventMapper;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import com.example.logisticstracking.infrastructure.persistence.repository.TrackingEventRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    @Cacheable(value = "packages", key = "#packageId")
    public PackageDetailsDTO execute(String packageId, boolean includeEvents) {

        PackageEntity packageEntity = packageRepository.findById(packageId)
                .orElseThrow(() -> new PackageNotFoundException(packageId));

        List<TrackingEventDTO> trackingEvents = includeEvents
                ? trackingEventRepository.findByPackageEntityIdOrderByDateAsc(packageId).stream()
                .map(trackingEventMapper::toDTO)
                .toList()
                : List.of();

        return packageMapper.toDetailsDTO(packageEntity, trackingEvents);
    }

}
