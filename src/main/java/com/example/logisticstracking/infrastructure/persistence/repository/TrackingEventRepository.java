package com.example.logisticstracking.infrastructure.persistence.repository;


import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingEventRepository extends JpaRepository<TrackingEventEntity, Long> {
    List<TrackingEventEntity> findByPackageId(String packageId);
}
