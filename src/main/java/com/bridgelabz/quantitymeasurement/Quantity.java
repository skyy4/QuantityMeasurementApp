package com.bridgelabz.quantitymeasurement;

public class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public static <U extends IMeasurable> double convert(double value, U sourceUnit, U targetUnit) {
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        double baseValue = sourceUnit.convertToBaseUnit(value);
        return targetUnit.convertFromBaseUnit(baseValue);
    }

    public Quantity<U> convertTo(U targetUnit) {
        double convertedValue = Quantity.convert(this.value, this.unit, targetUnit);
        return new Quantity<>(convertedValue, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Quantity<?> quantity = (Quantity<?>) obj;
        if (this.unit.getClass() != quantity.unit.getClass()) {
            return false;
        }

        // Use epsilon for robust double comparisons
        double epsilon = 1e-6;
        return Math.abs(compareValue(quantity) - compareValue(this)) < epsilon;
    }

    private double compareValue(Quantity<?> quantity) {
        return quantity.value * quantity.unit.getConversionFactor();
    }

    @Override
    public String toString() {
        return "Quantity{value=" + value + ", unit=" + unit + "}";
    }
}
