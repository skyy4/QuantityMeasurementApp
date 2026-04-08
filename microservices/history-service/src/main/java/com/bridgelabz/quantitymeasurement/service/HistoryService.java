package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;

import java.util.List;

public interface HistoryService {
    QuantityMeasurementDTO saveHistory(QuantityMeasurementDTO dto);

    List<QuantityMeasurementDTO> getAllHistory(Long userId);

    List<QuantityMeasurementDTO> getOperationHistory(Long userId, String operation);

    List<QuantityMeasurementDTO> getMeasurementsByType(Long userId, String measurementType);

    long getOperationCount(Long userId, String operation);

    List<QuantityMeasurementDTO> getErrorHistory(Long userId);
}
