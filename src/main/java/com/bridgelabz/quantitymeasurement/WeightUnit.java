package com.bridgelabz.quantitymeasurement;

public enum WeightUnit {
    KILOGRAM(1.0), GRAM(0.001), POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * this.conversionFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.conversionFactor;
    }
}
