package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HistoryServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(HistoryServiceClient.class);

    private final RestClient restClient;
    private final String apiKey;

    public HistoryServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${app.history-service.base-url}") String baseUrl,
            @Value("${app.history-service.api-key}") String apiKey
    ) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    public void saveHistory(QuantityMeasurementDTO dto) {
        try {
            restClient.post()
                    .uri("/internal/history")
                    .header("X-Internal-Api-Key", apiKey)
                    .body(dto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception exception) {
            logger.warn("Unable to persist history via history-service: {}", exception.getMessage());
        }
    }
}
