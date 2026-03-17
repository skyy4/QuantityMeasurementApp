package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.*;
import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    @Autowired
    private QuantityMeasurementRepository repository;

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO first, QuantityDTO second) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(first.getValue());
        dto.setThisUnit(first.getUnit());
        dto.setThisMeasurementType(first.getMeasurementType());
        dto.setThatValue(second.getValue());
        dto.setThatUnit(second.getUnit());
        dto.setThatMeasurementType(second.getMeasurementType());
        dto.setOperation("compare");

        try {
            QuantityModel<IMeasurable> left = convertDtoToModel(first);
            QuantityModel<IMeasurable> right = convertDtoToModel(second);
            boolean result = new Quantity<>(left.getValue(), left.getUnit())
                    .equals(new Quantity<>(right.getValue(), right.getUnit()));
            dto.setResultString(String.valueOf(result));
            dto.setResultValue(0.0);
        } catch (Exception e) {
            dto.setError(true);
            dto.setErrorMessage(e.getMessage());
        }
        repository.save(dto.toEntity());
        return dto;
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO source, String targetUnit) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(source.getValue());
        dto.setThisUnit(source.getUnit());
        dto.setThisMeasurementType(source.getMeasurementType());
        dto.setOperation("convert");

        try {
            QuantityModel<IMeasurable> model = convertDtoToModel(source);
            IMeasurable target = mapUnit(source.getMeasurementType(), targetUnit);
            Quantity<IMeasurable> quantity = new Quantity<>(model.getValue(), model.getUnit());
            Quantity<IMeasurable> converted = quantity.convertTo(target);
            dto.setResultValue(converted.getValue());
            dto.setResultUnit(targetUnit);
            dto.setResultMeasurementType(source.getMeasurementType());
        } catch (Exception e) {
            dto.setError(true);
            dto.setErrorMessage(e.getMessage());
        }
        repository.save(dto.toEntity());
        return dto;
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO first, QuantityDTO second) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(first.getValue());
        dto.setThisUnit(first.getUnit());
        dto.setThisMeasurementType(first.getMeasurementType());
        dto.setThatValue(second.getValue());
        dto.setThatUnit(second.getUnit());
        dto.setThatMeasurementType(second.getMeasurementType());
        dto.setOperation("add");

        try {
            QuantityModel<IMeasurable> left = convertDtoToModel(first);
            QuantityModel<IMeasurable> right = convertDtoToModel(second);
            Quantity<IMeasurable> q1 = new Quantity<>(left.getValue(), left.getUnit());
            Quantity<IMeasurable> q2 = new Quantity<>(right.getValue(), right.getUnit());
            Quantity<IMeasurable> sum = q1.add(q2, left.getUnit());
            dto.setResultValue(sum.getValue());
            dto.setResultUnit(first.getUnit());
            dto.setResultMeasurementType(first.getMeasurementType());
        } catch (Exception e) {
            dto.setError(true);
            dto.setErrorMessage(e.getMessage());
        }
        repository.save(dto.toEntity());
        return dto;
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO first, QuantityDTO second) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(first.getValue());
        dto.setThisUnit(first.getUnit());
        dto.setThisMeasurementType(first.getMeasurementType());
        dto.setThatValue(second.getValue());
        dto.setThatUnit(second.getUnit());
        dto.setThatMeasurementType(second.getMeasurementType());
        dto.setOperation("subtract");

        try {
            QuantityModel<IMeasurable> left = convertDtoToModel(first);
            QuantityModel<IMeasurable> right = convertDtoToModel(second);
            Quantity<IMeasurable> q1 = new Quantity<>(left.getValue(), left.getUnit());
            Quantity<IMeasurable> q2 = new Quantity<>(right.getValue(), right.getUnit());
            Quantity<IMeasurable> diff = q1.subtract(q2, left.getUnit());
            dto.setResultValue(diff.getValue());
            dto.setResultUnit(first.getUnit());
            dto.setResultMeasurementType(first.getMeasurementType());
        } catch (Exception e) {
            dto.setError(true);
            dto.setErrorMessage(e.getMessage());
        }
        repository.save(dto.toEntity());
        return dto;
    }

    @Override
    public QuantityMeasurementDTO divide(QuantityDTO first, QuantityDTO second) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(first.getValue());
        dto.setThisUnit(first.getUnit());
        dto.setThisMeasurementType(first.getMeasurementType());
        dto.setThatValue(second.getValue());
        dto.setThatUnit(second.getUnit());
        dto.setThatMeasurementType(second.getMeasurementType());
        dto.setOperation("divide");

        try {
            QuantityModel<IMeasurable> left = convertDtoToModel(first);
            QuantityModel<IMeasurable> right = convertDtoToModel(second);
            Quantity<IMeasurable> q1 = new Quantity<>(left.getValue(), left.getUnit());
            Quantity<IMeasurable> q2 = new Quantity<>(right.getValue(), right.getUnit());
            double result = q1.divide(q2);
            dto.setResultValue(result);
            dto.setResultString(String.valueOf(result));
        } catch (Exception e) {
            dto.setError(true);
            dto.setErrorMessage(e.getMessage());
        }
        repository.save(dto.toEntity());
        return dto;
    }

    @Override
    public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByOperation(operation));
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementsByType(String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByThisMeasurementType(measurementType));
    }

    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationAndIsErrorFalse(operation);
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findByIsErrorTrue());
    }

    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO dto) {
        IMeasurable unit = mapUnit(dto.getMeasurementType(), dto.getUnit());
        return new QuantityModel<>(dto.getValue(), unit);
    }

    private IMeasurable mapUnit(String type, String unitName) {
        try {
            if (type.equalsIgnoreCase("LengthUnit")) return LengthUnit.valueOf(unitName.toUpperCase());
            if (type.equalsIgnoreCase("WeightUnit")) return WeightUnit.valueOf(unitName.toUpperCase());
            if (type.equalsIgnoreCase("VolumeUnit")) return VolumeUnit.valueOf(unitName.toUpperCase());
            if (type.equalsIgnoreCase("TemperatureUnit")) return TemperatureUnit.valueOf(unitName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new QuantityMeasurementException("Unit must be valid for the specified measurement type");
        }
        throw new QuantityMeasurementException("Unsupported measurement type: " + type);
    }
}
