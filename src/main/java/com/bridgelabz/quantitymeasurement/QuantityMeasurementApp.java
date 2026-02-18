package com.bridgelabz.quantitymeasurement;

public class QuantityMeasurementApp {

    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Feet feet = (Feet) obj;
            return Double.compare(feet.value, value) == 0;
        }
    }

    public static void main(String[] args) {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);

        System.out.println("Input: 1.0 ft and 1.0 ft");
        if (feet1.equals(feet2)) {
            System.out.println("Output: Equal (true)");
        } else {
            System.out.println("Output: Not Equal (false)");
        }
    }
}
