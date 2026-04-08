package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {
    List<QuantityMeasurementEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<QuantityMeasurementEntity> findByUserIdAndOperationOrderByCreatedAtDesc(Long userId, String operation);

    List<QuantityMeasurementEntity> findByUserIdAndThisMeasurementTypeOrderByCreatedAtDesc(Long userId, String measurementType);

    List<QuantityMeasurementEntity> findByUserIdAndIsErrorTrueOrderByCreatedAtDesc(Long userId);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.userId = :userId AND q.operation = :operation AND q.isError = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(Long userId, String operation);

    long countByUserIdAndOperationAndIsErrorFalse(Long userId, String operation);
}
