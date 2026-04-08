package com.bridgelabz.quantitymeasurement;

/**
 * Custom unchecked exception representing domain specific quantity measurement
 * errors.
 */
public class QuantityMeasurementException extends RuntimeException {

    public QuantityMeasurementException(String message) {
        super(message);
    }

    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}

