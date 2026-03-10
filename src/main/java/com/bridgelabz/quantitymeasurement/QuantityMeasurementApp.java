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

        System.out.println("\n--- UC12: Subtraction and Division Examples ---");
        System.out.println("\nSubtraction Operations:");
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(6.0, LengthUnit.INCH), null);
        demonstrateSubtraction(new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5000.0, WeightUnit.GRAM),
                null);
        demonstrateSubtraction(new Quantity<>(5.0, VolumeUnit.LITRE), new Quantity<>(500.0, VolumeUnit.MILLILITRE),
                null);

        System.out.println("\nSubtraction with Explicit Target Unit:");
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(6.0, LengthUnit.INCH),
                LengthUnit.INCH);
        demonstrateSubtraction(new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5000.0, WeightUnit.GRAM),
                WeightUnit.GRAM);
        demonstrateSubtraction(new Quantity<>(5.0, VolumeUnit.LITRE), new Quantity<>(2.0, VolumeUnit.LITRE),
                VolumeUnit.MILLILITRE);

        System.out.println("\nSubtraction Resulting in Negative Values:");
        demonstrateSubtraction(new Quantity<>(5.0, LengthUnit.FEET), new Quantity<>(10.0, LengthUnit.FEET), null);
        demonstrateSubtraction(new Quantity<>(2.0, WeightUnit.KILOGRAM), new Quantity<>(5.0, WeightUnit.KILOGRAM),
                null);

        System.out.println("\nSubtraction Resulting in Zero:");
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(120.0, LengthUnit.INCH), null);
        demonstrateSubtraction(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                null);

        System.out.println("\nDivision Operations:");
        demonstrateDivision(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(2.0, LengthUnit.FEET));
        demonstrateDivision(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(5.0, LengthUnit.FEET));
        demonstrateDivision(new Quantity<>(24.0, LengthUnit.INCH), new Quantity<>(2.0, LengthUnit.FEET));
        demonstrateDivision(new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5.0, WeightUnit.KILOGRAM));
        demonstrateDivision(new Quantity<>(5.0, VolumeUnit.LITRE), new Quantity<>(10.0, VolumeUnit.LITRE));

        System.out.println("\nDivision with Different Units (Same Category):");
        demonstrateDivision(new Quantity<>(12.0, LengthUnit.INCH), new Quantity<>(1.0, LengthUnit.FEET));
        demonstrateDivision(new Quantity<>(2000.0, WeightUnit.GRAM), new Quantity<>(1.0, WeightUnit.KILOGRAM));
        demonstrateDivision(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), new Quantity<>(1.0, VolumeUnit.LITRE));

        System.out.println("\nError Cases (Testing Exceptions):");
        try {
            new Quantity<>(10.0, LengthUnit.FEET).subtract(null);
        } catch (Exception e) {
            System.out.println("Output: Throws " + e.getClass().getSimpleName());
        }
        try {
            new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET));
        } catch (Exception e) {
            System.out.println("Output: Throws " + e.getClass().getSimpleName());
        }
        try {
            new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, WeightUnit.KILOGRAM));
        } catch (Exception e) {
            System.out.println("Output: Throws " + e.getClass().getSimpleName());
        }
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

    public static <U extends IMeasurable> void demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        String targetName = (targetUnit == null) ? q1.getUnit().getUnitName() : targetUnit.getUnitName();
        System.out.println("Input: " + q1 + ".subtract(" + q2 + (targetUnit != null ? ", " + targetName : "") + ")");
        if (q2 == null) {
            System.out.println("Output: Throws IllegalArgumentException");
            return;
        }
        Quantity<U> result = (targetUnit == null) ? q1.subtract(q2) : q1.subtract(q2, targetUnit);
        System.out.println("Output: " + result);
    }

    public static <U extends IMeasurable> void demonstrateDivision(Quantity<U> q1, Quantity<U> q2) {
        System.out.println("Input: " + q1 + ".divide(" + q2 + ")");
        double result = q1.divide(q2);
        System.out.println("Output: " + result);
    }
}
