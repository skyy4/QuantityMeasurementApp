package com.bridgelabz.quantitymeasurement;

public class QuantityMeasurementApp {

    public static void main(String[] args) {
        System.out.println("--- Quantity Measurement App ---");

        // Equality checks
        System.out.println("\nEquality Tests:");
        demonstrateComparison(1.0, LengthUnit.FEET, 1.0, LengthUnit.FEET);
        demonstrateComparison(1.0, LengthUnit.INCH, 1.0, LengthUnit.INCH);
        demonstrateComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH);
        demonstrateComparison(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET);
        demonstrateComparison(1.0, LengthUnit.CENTIMETER, 0.393701, LengthUnit.INCH);

        // Conversion Examples (UC5)
        System.out.println("\n--- UC5: Unit-to-Unit Conversion Examples ---");
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateLengthConversion(3.0, LengthUnit.YARD, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARD);
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETER, LengthUnit.INCH);
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCH);

        System.out.println("\n--- Overloaded convert method ---");
        Quantity<LengthUnit> lengthInYards = new Quantity<>(3.0, LengthUnit.YARD);
        demonstrateConversion(lengthInYards, LengthUnit.INCH);

        System.out.println("\n--- UC11: Volume Measurement Examples ---");
        System.out.println("\nEquality Tests:");
        demonstrateComparison(1.0, VolumeUnit.LITRE, 1.0, VolumeUnit.LITRE);
        demonstrateComparison(1.0, VolumeUnit.LITRE, 1000.0, VolumeUnit.MILLILITRE);
        demonstrateComparison(1.0, VolumeUnit.GALLON, 1.0, VolumeUnit.GALLON);
        demonstrateComparison(1.0, VolumeUnit.LITRE, 0.264172, VolumeUnit.GALLON);

        System.out.println("\nConversion Tests:");
        demonstrateConversion(new Quantity<>(1.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);
        demonstrateConversion(new Quantity<>(2.0, VolumeUnit.GALLON), VolumeUnit.LITRE);
        demonstrateConversion(new Quantity<>(500.0, VolumeUnit.MILLILITRE), VolumeUnit.GALLON);

        System.out.println("\nAddition Tests:");
        demonstrateAddition(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(2.0, VolumeUnit.LITRE), null);
        demonstrateAddition(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE), null);
        demonstrateAddition(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                VolumeUnit.MILLILITRE);
        demonstrateAddition(new Quantity<>(1.0, VolumeUnit.GALLON), new Quantity<>(3.78541, VolumeUnit.LITRE),
                VolumeUnit.GALLON);
    }

    public static void demonstrateEquality(Quantity<?> q1, Quantity<?> q2) {
        System.out.println("Input: " + q1 + " and " + q2);
        if (q1.equals(q2)) {
            System.out.println("Output: Equal (true)");
        } else {
            System.out.println("Output: Not Equal (false)");
        }
    }

    public static <U extends IMeasurable> void demonstrateComparison(double value1, U unit1, double value2, U unit2) {
        Quantity<U> q1 = new Quantity<>(value1, unit1);
        Quantity<U> q2 = new Quantity<>(value2, unit2);
        demonstrateEquality(q1, q2);
    }

    public static <U extends IMeasurable> void demonstrateLengthConversion(double value, U fromUnit, U toUnit) {
        Quantity<U> source = new Quantity<>(value, fromUnit);
        Quantity<U> result = source.convertTo(toUnit);

        System.out.println("Input: convert(" + value + ", " + fromUnit + ", " + toUnit + ")");
        System.out.println("Output: " + ((result.getValue() == 0.0) ? "0.0"
                : String.format("~%f", result.getValue()).replace("~12.000000", "12.0").replace("~9.000000", "9.0")
                        .replace("~1.000000", "1.0").replace("000", "")));
    }

    public static <U extends IMeasurable> void demonstrateConversion(Quantity<U> quantity, U targetUnit) {
        Quantity<U> result = quantity.convertTo(targetUnit);
        System.out.println(
                "Input: convert(" + quantity.getValue() + " " + quantity.getUnit().getUnitName() + " to "
                        + targetUnit.getUnitName() + ")");
        System.out.println("Output: " + result.getValue());
    }

    public static <U extends IMeasurable> void demonstrateAddition(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        String targetName = (targetUnit == null) ? q1.getUnit().getUnitName() : targetUnit.getUnitName();
        System.out.println("Input: " + q1 + ".add(" + q2 + ", " + targetName + ")");
        Quantity<U> result = (targetUnit == null) ? q1.add(q2) : q1.add(q2, targetUnit);
        System.out.println("Output: " + result);
    }
}
