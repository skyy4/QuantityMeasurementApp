package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class ResultCacheService {

    private static final Logger logger = LoggerFactory.getLogger(ResultCacheService.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Duration ttl;

    public ResultCacheService(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            @Value("${app.cache.result-ttl-minutes:30}") long resultTtlMinutes
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.ttl = Duration.ofMinutes(resultTtlMinutes);
    }

    public Optional<QuantityMeasurementDTO> get(String key) {
        try {
            String payload = redisTemplate.opsForValue().get(key);
            if (payload == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(payload, QuantityMeasurementDTO.class));
        } catch (Exception exception) {
            logger.warn("Unable to read result cache for key {}: {}", key, exception.getMessage());
            return Optional.empty();
        }
    }

    public void put(String key, QuantityMeasurementDTO value) {
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), ttl);
        } catch (JsonProcessingException exception) {
            logger.warn("Unable to serialize result cache for key {}: {}", key, exception.getMessage());
        } catch (Exception exception) {
            logger.warn("Unable to write result cache for key {}: {}", key, exception.getMessage());
        }
    }
}
