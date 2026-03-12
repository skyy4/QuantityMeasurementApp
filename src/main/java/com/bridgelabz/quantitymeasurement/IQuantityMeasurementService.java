package com.bridgelabz.quantitymeasurement;

/**
 * Service abstraction for quantity measurement operations.
 * <p>
 * All methods accept and return {@link QuantityDTO} instances to provide a
 * stable contract between layers and potential external clients.
 */
public interface IQuantityMeasurementService {

    /**
     * Compare two quantities for equality.
     */
    boolean compare(QuantityDTO first, QuantityDTO second);

    /**
     * Convert a quantity to a different unit within the same measurement type.
     */
    QuantityDTO convert(QuantityDTO source, QuantityDTO.IMeasurableUnit targetUnit);

    /**
     * Add two quantities and optionally convert the result to a target unit. If
     * {@code targetUnit} is {@code null}, the unit of the first operand is used.
     */
    QuantityDTO add(QuantityDTO first, QuantityDTO second, QuantityDTO.IMeasurableUnit targetUnit);

    /**
     * Subtract the second quantity from the first and optionally convert the
     * result to a target unit. If {@code targetUnit} is {@code null}, the unit
     * of the first operand is used.
     */
    QuantityDTO subtract(QuantityDTO first, QuantityDTO second, QuantityDTO.IMeasurableUnit targetUnit);

    /**
     * Divide the first quantity by the second and return a dimensionless scalar.
     */
    double divide(QuantityDTO first, QuantityDTO second);
}

