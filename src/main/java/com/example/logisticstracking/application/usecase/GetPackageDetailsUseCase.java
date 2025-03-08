package com.example.logisticstracking.application.usecase;

import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.application.dto.TrackingEventDTO;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import com.example.logisticstracking.infrastructure.persistence.repository.PackageRepository;
import com.example.logisticstracking.infrastructure.persistence.repository.TrackingEventRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GetPackageDetailsUseCase {

    private final PackageRepository packageRepository;
    private final TrackingEventRepository trackingEventRepository;

    public GetPackageDetailsUseCase(PackageRepository packageRepository,
                                    TrackingEventRepository trackingEventRepository) {
        this.packageRepository = packageRepository;
        this.trackingEventRepository = trackingEventRepository;
    }

    public PackageDetailsDTO execute(String packageId, boolean includeEvents) {
        PackageEntity pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Pacote não encontrado: " + packageId));

        List<TrackingEventDTO> eventDTOs = includeEvents
                ? trackingEventRepository.findByPackageId(packageId).stream()
                .map(e -> new TrackingEventDTO(
                        e.getId(),
                        e.getPackageId(),
                        e.getLocation(),
                        e.getDescription(),
                        e.getDate()))
                .collect(Collectors.toList())
                : List.of();

        return new PackageDetailsDTO(
                pkg.getId(),
                pkg.getDescription(),
                pkg.getSender(),
                pkg.getRecipient(),
                pkg.getStatus(),
                pkg.getCreatedAt(),
                pkg.getUpdatedAt(),
                pkg.getDeliveredAt(),
                pkg.isHoliday(),
                pkg.getFunFact(),
                pkg.getEstimatedDeliveryDate(),
                eventDTOs
        );
    }
}
