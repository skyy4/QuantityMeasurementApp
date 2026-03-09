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
}
