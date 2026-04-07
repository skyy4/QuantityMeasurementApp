package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;

public final class CacheKeyFactory {

    private CacheKeyFactory() {
    }

    public static String operationKey(Long userId, String operation, QuantityDTO first, QuantityDTO second, String targetUnit) {
        StringBuilder builder = new StringBuilder("qma:result:")
                .append(userId).append(':')
                .append(operation).append(':')
                .append(first.getMeasurementType()).append(':')
                .append(first.getUnit()).append(':')
                .append(first.getValue());

        if (second != null) {
            builder.append(':').append(second.getUnit())
                    .append(':').append(second.getValue());
        }

        if (targetUnit != null) {
            builder.append(':').append(targetUnit);
        }

        return builder.toString();
    }

    public static String historyKey(Long userId, String operation, String measurementType, boolean errorsOnly) {
        return "qma:history:" + userId + ':' + safe(operation) + ':' + safe(measurementType) + ':' + errorsOnly;
    }

    private static String safe(String value) {
        return value == null ? "all" : value.toLowerCase();
    }
}
