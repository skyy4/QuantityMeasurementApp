package com.bridgelabz.quantitymeasurement;

import java.util.List;

/**
 * Repository abstraction for persisting {@link QuantityMeasurementEntity}
 * objects. Implementations can be in-memory, file-based, database-backed, etc.
 */
public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();

    /**
     * Retrieve all measurements filtered by operation type.
     */
    List<QuantityMeasurementEntity> getMeasurementsByOperation(QuantityMeasurementEntity.OperationType operationType);

    /**
     * Retrieve all measurements filtered by high level measurement category.
     */
    List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(QuantityDTO.MeasurementType measurementType);

    /**
     * Total number of stored measurements.
     */
    long getTotalCount();

    /**
     * Remove all measurements. Primarily intended for tests and demos.
     */
    void deleteAll();

    /**
     * Optional hook for implementations that use connection pooling to expose
     * basic statistics. Default implementation returns an empty string.
     */
    default String getPoolStatistics() {
        return "";
    }

    /**
     * Optional hook to release resources such as database connections when the
     * application is shutting down.
     */
    default void releaseResources() {
        // no-op by default
    }
}


