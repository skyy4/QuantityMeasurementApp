package com.bridgelabz.quantitymeasurement;

/**
 * Thin controller layer that exposes high level operations on
 * {@link QuantityDTO} and delegates all business logic to the service layer.
 * <p>
 * This class is designed so that it can later be adapted as a REST controller
 * without changing the underlying service implementation.
 */
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
    }

    public boolean performComparison(QuantityDTO first, QuantityDTO second) {
        return service.compare(first, second);
    }

    public QuantityDTO performConversion(QuantityDTO source, QuantityDTO.IMeasurableUnit targetUnit) {
        return service.convert(source, targetUnit);
    }

    public QuantityDTO performAddition(QuantityDTO first, QuantityDTO second, QuantityDTO.IMeasurableUnit targetUnit) {
        return service.add(first, second, targetUnit);
    }

    public QuantityDTO performSubtraction(QuantityDTO first, QuantityDTO second,
                                          QuantityDTO.IMeasurableUnit targetUnit) {
        return service.subtract(first, second, targetUnit);
    }

    public double performDivision(QuantityDTO first, QuantityDTO second) {
        return service.divide(first, second);
    }
}

