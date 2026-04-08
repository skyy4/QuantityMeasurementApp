package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import java.util.List;

public interface IQuantityMeasurementService {
    QuantityMeasurementDTO compare(Long userId, String userEmail, QuantityDTO first, QuantityDTO second);

    QuantityMeasurementDTO convert(Long userId, String userEmail, QuantityDTO source, String targetUnit);

    QuantityMeasurementDTO add(Long userId, String userEmail, QuantityDTO first, QuantityDTO second);

    QuantityMeasurementDTO subtract(Long userId, String userEmail, QuantityDTO first, QuantityDTO second);

    QuantityMeasurementDTO divide(Long userId, String userEmail, QuantityDTO first, QuantityDTO second);
}
