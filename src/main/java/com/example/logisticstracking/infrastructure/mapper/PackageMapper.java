package com.example.logisticstracking.infrastructure.mapper;

import com.example.logisticstracking.domain.entity.Package;
import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PackageMapper {

    public PackageEntity toEntity(Package domain) {
        return new PackageEntity(
                domain.id(),
                domain.description(),
                domain.sender(),
                domain.recipient(),
                domain.status(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deliveredAt(),
                domain.isHoliday(),
                domain.funFact(),
                domain.estimatedDeliveryDate()
        );
    }

    public Package toDomain(PackageEntity entity) {
        return new Package(
                entity.getId(),
                entity.getDescription(),
                entity.getSender(),
                entity.getRecipient(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeliveredAt(),
                entity.isHoliday(),
                entity.getFunFact(),
                entity.getEstimatedDeliveryDate(),
                List.of()
        );
    }
}
