package com.example.logisticstracking.infrastructure.persistence.repository;

import com.example.logisticstracking.infrastructure.persistence.entity.TrackingEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEventEntity, UUID> {

    List<TrackingEventEntity> findByPackageEntityIdOrderByDateAsc(String packageEntityId);

}
