package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.*;
import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import org.springframework.stereotype.Service;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final ResultCacheService resultCacheService;
    private final HistoryServiceClient historyServiceClient;

    public QuantityMeasurementServiceImpl(
            ResultCacheService resultCacheService,
            HistoryServiceClient historyServiceClient
    ) {
        this.resultCacheService = resultCacheService;
        this.historyServiceClient = historyServiceClient;
    }

    @Override
    public QuantityMeasurementDTO compare(Long userId, String userEmail, QuantityDTO first, QuantityDTO second) {
        String cacheKey = CacheKeyFactory.operationKey(userId, "compare", first, second, null);
        QuantityMeasurementDTO dto = resultCacheService.get(cacheKey)
                .map(this::copyDto)
                .orElseGet(() -> {
                    QuantityMeasurementDTO computed = buildBaseDto(first, second, "compare");
                    try {
                        QuantityModel<IMeasurable> left = convertDtoToModel(first);
                        QuantityModel<IMeasurable> right = convertDtoToModel(second);
                        boolean result = new Quantity<>(left.getValue(), left.getUnit())
                                .equals(new Quantity<>(right.getValue(), right.getUnit()));
                        computed.setResultString(String.valueOf(result));
                        computed.setResultValue(0.0);
                    } catch (Exception exception) {
                        computed.setError(true);
                        computed.setErrorMessage(exception.getMessage());
                    }
                    resultCacheService.put(cacheKey, computed);
                    return computed;
                });

        return persistForUser(dto, userId, userEmail);
    }

    @Override
    public QuantityMeasurementDTO convert(Long userId, String userEmail, QuantityDTO source, String targetUnit) {
        String cacheKey = CacheKeyFactory.operationKey(userId, "convert", source, null, targetUnit);
        QuantityMeasurementDTO dto = resultCacheService.get(cacheKey)
                .map(this::copyDto)
                .orElseGet(() -> {
                    QuantityMeasurementDTO computed = buildBaseDto(source, null, "convert");
                    try {
                        QuantityModel<IMeasurable> model = convertDtoToModel(source);
                        IMeasurable target = mapUnit(source.getMeasurementType(), targetUnit);
                        Quantity<IMeasurable> quantity = new Quantity<>(model.getValue(), model.getUnit());
                        Quantity<IMeasurable> converted = quantity.convertTo(target);
                        computed.setResultValue(converted.getValue());
                        computed.setResultUnit(targetUnit);
                        computed.setResultMeasurementType(source.getMeasurementType());
                    } catch (Exception exception) {
                        computed.setError(true);
                        computed.setErrorMessage(exception.getMessage());
                    }
                    resultCacheService.put(cacheKey, computed);
                    return computed;
                });

        return persistForUser(dto, userId, userEmail);
    }

    @Override
    public QuantityMeasurementDTO add(Long userId, String userEmail, QuantityDTO first, QuantityDTO second) {
        return computeArithmetic(userId, userEmail, "add", first, second);
    }

    @Override
    public QuantityMeasurementDTO subtract(Long userId, String userEmail, QuantityDTO first, QuantityDTO second) {
        return computeArithmetic(userId, userEmail, "subtract", first, second);
    }

    @Override
    public QuantityMeasurementDTO divide(Long userId, String userEmail, QuantityDTO first, QuantityDTO second) {
        String cacheKey = CacheKeyFactory.operationKey(userId, "divide", first, second, null);
        QuantityMeasurementDTO dto = resultCacheService.get(cacheKey)
                .map(this::copyDto)
                .orElseGet(() -> {
                    QuantityMeasurementDTO computed = buildBaseDto(first, second, "divide");
                    try {
                        QuantityModel<IMeasurable> left = convertDtoToModel(first);
                        QuantityModel<IMeasurable> right = convertDtoToModel(second);
                        Quantity<IMeasurable> q1 = new Quantity<>(left.getValue(), left.getUnit());
                        Quantity<IMeasurable> q2 = new Quantity<>(right.getValue(), right.getUnit());
                        double result = q1.divide(q2);
                        computed.setResultValue(result);
                        computed.setResultString(String.valueOf(result));
                    } catch (Exception exception) {
                        computed.setError(true);
                        computed.setErrorMessage(exception.getMessage());
                    }
                    resultCacheService.put(cacheKey, computed);
                    return computed;
                });

        return persistForUser(dto, userId, userEmail);
    }

    private QuantityMeasurementDTO computeArithmetic(
            Long userId,
            String userEmail,
            String operation,
            QuantityDTO first,
            QuantityDTO second
    ) {
        String cacheKey = CacheKeyFactory.operationKey(userId, operation, first, second, null);
        QuantityMeasurementDTO dto = resultCacheService.get(cacheKey)
                .map(this::copyDto)
                .orElseGet(() -> {
                    QuantityMeasurementDTO computed = buildBaseDto(first, second, operation);
                    try {
                        QuantityModel<IMeasurable> left = convertDtoToModel(first);
                        QuantityModel<IMeasurable> right = convertDtoToModel(second);
                        Quantity<IMeasurable> q1 = new Quantity<>(left.getValue(), left.getUnit());
                        Quantity<IMeasurable> q2 = new Quantity<>(right.getValue(), right.getUnit());
                        Quantity<IMeasurable> result = "add".equals(operation)
                                ? q1.add(q2, left.getUnit())
                                : q1.subtract(q2, left.getUnit());
                        computed.setResultValue(result.getValue());
                        computed.setResultUnit(first.getUnit());
                        computed.setResultMeasurementType(first.getMeasurementType());
                    } catch (Exception exception) {
                        computed.setError(true);
                        computed.setErrorMessage(exception.getMessage());
                    }
                    resultCacheService.put(cacheKey, computed);
                    return computed;
                });

        return persistForUser(dto, userId, userEmail);
    }

    private QuantityMeasurementDTO buildBaseDto(QuantityDTO first, QuantityDTO second, String operation) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(first.getValue());
        dto.setThisUnit(first.getUnit());
        dto.setThisMeasurementType(first.getMeasurementType());
        dto.setOperation(operation);

        if (second != null) {
            dto.setThatValue(second.getValue());
            dto.setThatUnit(second.getUnit());
            dto.setThatMeasurementType(second.getMeasurementType());
        }

        return dto;
    }

    private QuantityMeasurementDTO persistForUser(QuantityMeasurementDTO dto, Long userId, String userEmail) {
        QuantityMeasurementDTO copy = copyDto(dto);
        copy.setUserId(userId);
        copy.setUserEmail(userEmail);
        historyServiceClient.saveHistory(copy);
        return copy;
    }

    private QuantityMeasurementDTO copyDto(QuantityMeasurementDTO dto) {
        return QuantityMeasurementDTO.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .userEmail(dto.getUserEmail())
                .thisValue(dto.getThisValue())
                .thisUnit(dto.getThisUnit())
                .thisMeasurementType(dto.getThisMeasurementType())
                .thatValue(dto.getThatValue())
                .thatUnit(dto.getThatUnit())
                .thatMeasurementType(dto.getThatMeasurementType())
                .operation(dto.getOperation())
                .resultString(dto.getResultString())
                .resultValue(dto.getResultValue())
                .resultUnit(dto.getResultUnit())
                .resultMeasurementType(dto.getResultMeasurementType())
                .errorMessage(dto.getErrorMessage())
                .error(dto.isError())
                .createdAt(dto.getCreatedAt())
                .build();
    }

    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO dto) {
        IMeasurable unit = mapUnit(dto.getMeasurementType(), dto.getUnit());
        return new QuantityModel<>(dto.getValue(), unit);
    }

    private IMeasurable mapUnit(String type, String unitName) {
        try {
            String upperType = type.toUpperCase();
            if (upperType.equals("LENGTH") || upperType.equals("LENGTHUNIT")) {
                return LengthUnit.valueOf(unitName.toUpperCase());
            }
            if (upperType.equals("WEIGHT") || upperType.equals("WEIGHTUNIT")) {
                return WeightUnit.valueOf(unitName.toUpperCase());
            }
            if (upperType.equals("VOLUME") || upperType.equals("VOLUMEUNIT")) {
                return VolumeUnit.valueOf(unitName.toUpperCase());
            }
            if (upperType.equals("TEMPERATURE") || upperType.equals("TEMPERATUREUNIT")) {
                return TemperatureUnit.valueOf(unitName.toUpperCase());
            }
        } catch (IllegalArgumentException exception) {
            throw new QuantityMeasurementException("Unit '" + unitName + "' is invalid for " + type);
        }

        throw new QuantityMeasurementException("Unsupported measurement type: " + type);
    }
}
