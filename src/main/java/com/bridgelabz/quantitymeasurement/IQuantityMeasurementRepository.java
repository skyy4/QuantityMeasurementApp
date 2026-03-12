package com.bridgelabz.quantitymeasurement;

import java.util.List;

/**
 * Repository abstraction for persisting {@link QuantityMeasurementEntity}
 * objects. Current implementation uses an in-memory cache with optional
 * serialization to disk, but the interface allows alternative implementations
 * (database, remote service, etc.).
 */
public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();
}

