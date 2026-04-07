package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.security.AuthenticatedUser;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
@Tag(name = "Quantity History", description = "User-scoped history endpoints")
public class HistoryController {

    private final IQuantityMeasurementService service;

    public HistoryController(IQuantityMeasurementService service) {
        this.service = service;
    }

    @GetMapping("/me")
    @Operation(summary = "Get all quantity history for the authenticated user")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMyHistory(Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.getAllHistory(user.getUserId()));
    }

    @GetMapping("/me/operation/{operation}")
    @Operation(summary = "Get history by operation for the authenticated user")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(
            Authentication authentication,
            @PathVariable String operation
    ) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.getOperationHistory(user.getUserId(), operation));
    }

    @GetMapping("/me/type/{type}")
    @Operation(summary = "Get history by measurement type for the authenticated user")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementsByType(
            Authentication authentication,
            @PathVariable String type
    ) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.getMeasurementsByType(user.getUserId(), type));
    }

    @GetMapping("/me/count/{operation}")
    @Operation(summary = "Get successful operation count for the authenticated user")
    public ResponseEntity<Long> getOperationCount(Authentication authentication, @PathVariable String operation) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.getOperationCount(user.getUserId(), operation));
    }

    @GetMapping("/me/errors")
    @Operation(summary = "Get errored history for the authenticated user")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory(Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.getErrorHistory(user.getUserId()));
    }
}
