package com.example.logisticstracking.infrastructure.persistence.repository;

import com.example.logisticstracking.infrastructure.persistence.entity.PackageEntity;
import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PackageRepository extends JpaRepository<PackageEntity, String> {
     List<PackageEntity> findBySender(String sender);
     List<PackageEntity> findByRecipient(String recipient);
     List<PackageEntity> findBySenderAndRecipient(String sender, String recipient);
     @Query("SELECT p FROM PackageEntity p LEFT JOIN FETCH p.trackingEvents WHERE p.id = :id")
     Optional<PackageEntity> findByIdWithEvents(@Param("id") String id);
}

