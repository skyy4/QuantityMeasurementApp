package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResultCacheService {

    private static final Logger logger = LoggerFactory.getLogger(ResultCacheService.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Duration ttl;
    private final boolean useRedis;
    private final Map<String, CacheEntry> inMemoryCache = new ConcurrentHashMap<>();

    public ResultCacheService(
            ObjectProvider<StringRedisTemplate> redisTemplateProvider,
            ObjectMapper objectMapper,
            @Value("${app.cache.result-ttl-minutes:30}") long resultTtlMinutes,
            @Value("${spring.cache.type:simple}") String cacheType
    ) {
        this.redisTemplate = redisTemplateProvider.getIfAvailable();
        this.objectMapper = objectMapper;
        this.ttl = Duration.ofMinutes(resultTtlMinutes);
        this.useRedis = "redis".equalsIgnoreCase(cacheType) && this.redisTemplate != null;
    }

    public Optional<QuantityMeasurementDTO> get(String key) {
        if (useRedis) {
            Optional<QuantityMeasurementDTO> cached = getFromRedis(key);
            if (cached.isPresent()) {
                return cached;
            }
        }

        return getFromMemory(key);
    }

    public void put(String key, QuantityMeasurementDTO value) {
        if (useRedis) {
            putInRedis(key, value);
        }

        putInMemory(key, value);
    }

    private Optional<QuantityMeasurementDTO> getFromRedis(String key) {
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

    private void putInRedis(String key, QuantityMeasurementDTO value) {
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), ttl);
        } catch (JsonProcessingException exception) {
            logger.warn("Unable to serialize result cache for key {}: {}", key, exception.getMessage());
        } catch (Exception exception) {
            logger.warn("Unable to write result cache for key {}: {}", key, exception.getMessage());
        }
    }

    private Optional<QuantityMeasurementDTO> getFromMemory(String key) {
        CacheEntry entry = inMemoryCache.get(key);
        if (entry == null) {
            return Optional.empty();
        }
        if (entry.expiresAt().isBefore(Instant.now())) {
            inMemoryCache.remove(key);
            return Optional.empty();
        }
        return Optional.of(entry.value());
    }

    private void putInMemory(String key, QuantityMeasurementDTO value) {
        inMemoryCache.put(key, new CacheEntry(value, Instant.now().plus(ttl)));
    }

    private record CacheEntry(QuantityMeasurementDTO value, Instant expiresAt) {
    }
}
