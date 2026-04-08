package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final QuantityMeasurementRepository repository;

    public HistoryServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuantityMeasurementDTO saveHistory(QuantityMeasurementDTO dto) {
        return QuantityMeasurementDTO.fromEntity(repository.save(Objects.requireNonNull(dto.toEntity())));
    }

    @Override
    public List<QuantityMeasurementDTO> getAllHistory(Long userId) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @Override
    public List<QuantityMeasurementDTO> getOperationHistory(Long userId, String operation) {
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByUserIdAndOperationOrderByCreatedAtDesc(userId, operation)
        );
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementsByType(Long userId, String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByUserIdAndThisMeasurementTypeOrderByCreatedAtDesc(userId, measurementType)
        );
    }

    @Override
    public long getOperationCount(Long userId, String operation) {
        return repository.countByUserIdAndOperationAndIsErrorFalse(userId, operation);
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory(Long userId) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByUserIdAndIsErrorTrueOrderByCreatedAtDesc(userId));
    }
}
