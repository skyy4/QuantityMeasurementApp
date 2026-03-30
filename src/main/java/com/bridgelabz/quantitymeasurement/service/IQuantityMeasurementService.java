package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import java.util.List;

public interface IQuantityMeasurementService {
    QuantityMeasurementDTO compare(QuantityDTO first, QuantityDTO second);

    QuantityMeasurementDTO convert(QuantityDTO source, String targetUnit);

    QuantityMeasurementDTO add(QuantityDTO first, QuantityDTO second);

    QuantityMeasurementDTO subtract(QuantityDTO first, QuantityDTO second);

    QuantityMeasurementDTO divide(QuantityDTO first, QuantityDTO second);

    List<QuantityMeasurementDTO> getOperationHistory(String operation);

    List<QuantityMeasurementDTO> getMeasurementsByType(String measurementType);

    long getOperationCount(String operation);

    List<QuantityMeasurementDTO> getErrorHistory();
}
