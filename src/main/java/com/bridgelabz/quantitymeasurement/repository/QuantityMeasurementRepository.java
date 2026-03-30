package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {
    List<QuantityMeasurementEntity> findByOperation(String operation);

    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.operation = :operation AND q.isError = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(String operation);

    long countByOperationAndIsErrorFalse(String operation);

    List<QuantityMeasurementEntity> findByIsErrorTrue();
}
