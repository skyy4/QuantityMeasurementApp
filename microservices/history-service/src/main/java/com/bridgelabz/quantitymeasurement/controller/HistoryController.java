package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.security.AuthenticatedUser;
import com.bridgelabz.quantitymeasurement.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Tag(name = "Quantity History", description = "History persistence and query endpoints")
public class HistoryController {

    private final HistoryService historyService;

    @Value("${app.internal.api-key}")
    private String internalApiKey;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping("/internal/history")
    @Operation(summary = "Internal endpoint used by qma-service to persist history")
    public ResponseEntity<QuantityMeasurementDTO> saveHistory(
            @RequestHeader(name = "X-Internal-Api-Key", required = false) String providedApiKey,
            @RequestBody QuantityMeasurementDTO dto
    ) {
        validateInternalApiKey(providedApiKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(historyService.saveHistory(dto));
    }

    @GetMapping("/api/v1/history/me")
    @Operation(summary = "Get all quantity history for the authenticated user")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMyHistory(Authentication authentication) {
        AuthenticatedUser user = extractAuthenticatedUser(authentication);
        return ResponseEntity.ok(historyService.getAllHistory(user.getUserId()));
    }

    @GetMapping("/api/v1/history/me/operation/{operation}")
    @Operation(summary = "Get history by operation for the authenticated user")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(
            Authentication authentication,
            @PathVariable String operation
    ) {
        AuthenticatedUser user = extractAuthenticatedUser(authentication);
        return ResponseEntity.ok(historyService.getOperationHistory(user.getUserId(), operation));
    }

    @GetMapping("/api/v1/history/me/type/{type}")
    @Operation(summary = "Get history by measurement type for the authenticated user")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementsByType(
            Authentication authentication,
            @PathVariable String type
    ) {
        AuthenticatedUser user = extractAuthenticatedUser(authentication);
        return ResponseEntity.ok(historyService.getMeasurementsByType(user.getUserId(), type));
    }

    @GetMapping("/api/v1/history/me/count/{operation}")
    @Operation(summary = "Get successful operation count for the authenticated user")
    public ResponseEntity<Long> getOperationCount(Authentication authentication, @PathVariable String operation) {
        AuthenticatedUser user = extractAuthenticatedUser(authentication);
        return ResponseEntity.ok(historyService.getOperationCount(user.getUserId(), operation));
    }

    @GetMapping("/api/v1/history/me/errors")
    @Operation(summary = "Get errored history for the authenticated user")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory(Authentication authentication) {
        AuthenticatedUser user = extractAuthenticatedUser(authentication);
        return ResponseEntity.ok(historyService.getErrorHistory(user.getUserId()));
    }

    private void validateInternalApiKey(String providedApiKey) {
        if (providedApiKey == null || !providedApiKey.equals(internalApiKey)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid internal API key");
        }
    }

    private AuthenticatedUser extractAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof AuthenticatedUser user) || user.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication token");
        }

        return user;
    }
}
