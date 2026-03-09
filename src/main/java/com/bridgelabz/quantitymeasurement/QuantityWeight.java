package com.bridgelabz.quantitymeasurement;

public class QuantityWeight {
    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
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

    public WeightUnit getUnit() {
        return unit;
    }

    public static double convert(double value, WeightUnit sourceUnit, WeightUnit targetUnit) {
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        double baseValue = sourceUnit.convertToBaseUnit(value);
        return targetUnit.convertFromBaseUnit(baseValue);
    }

    public QuantityWeight convertTo(WeightUnit targetUnit) {
        double convertedValue = QuantityWeight.convert(this.value, this.unit, targetUnit);
        return new QuantityWeight(convertedValue, targetUnit);
    }

    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double sumInBase = getSumInBaseUnit(other);
        double sumInTargetUnit = targetUnit.convertFromBaseUnit(sumInBase);

        return new QuantityWeight(sumInTargetUnit, targetUnit);
    }

    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.unit);
    }

    private double getSumInBaseUnit(QuantityWeight other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add a null quantity");
        }

        double thisInBase = this.unit.convertToBaseUnit(this.value);
        double otherInBase = other.unit.convertToBaseUnit(other.value);
        return thisInBase + otherInBase;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        QuantityWeight quantity = (QuantityWeight) obj;

        // Use epsilon for robust double comparisons
        double epsilon = 1e-6;
        return Math.abs(compareValue(quantity) - compareValue(this)) < epsilon;
    }

    private double compareValue(QuantityWeight quantity) {
        return quantity.unit.convertToBaseUnit(quantity.value);
    }

    @Override
    public String toString() {
        return "QuantityWeight{value=" + value + ", unit=" + unit + "}";
    }
}
