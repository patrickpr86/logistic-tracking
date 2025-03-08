package com.example.logisticstracking.infrastructure.persistence.repository;

import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<PackageEntity, String> {
     List<PackageEntity> findBySender(String sender);
     List<PackageEntity> findByRecipient(String recipient);
}
