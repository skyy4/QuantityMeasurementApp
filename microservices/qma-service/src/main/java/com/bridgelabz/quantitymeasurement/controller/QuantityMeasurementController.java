package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.QuantityInputDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.security.AuthenticatedUser;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public ResponseEntity<QuantityMeasurementDTO> compareQuantities(
            Authentication authentication,
            @Valid @RequestBody QuantityInputDTO input
    ) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.compare(
                user.getUserId(),
                user.getEmail(),
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO()
        ));
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to a different unit")
    public ResponseEntity<QuantityMeasurementDTO> convertQuantity(
            Authentication authentication,
            @Valid @RequestBody QuantityInputDTO input
    ) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.convert(
                user.getUserId(),
                user.getEmail(),
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO().getUnit()
        ));
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(
            Authentication authentication,
            @Valid @RequestBody QuantityInputDTO input
    ) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.add(
                user.getUserId(),
                user.getEmail(),
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO()
        ));
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(
            Authentication authentication,
            @Valid @RequestBody QuantityInputDTO input
    ) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.subtract(
                user.getUserId(),
                user.getEmail(),
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO()
        ));
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(
            Authentication authentication,
            @Valid @RequestBody QuantityInputDTO input
    ) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(service.divide(
                user.getUserId(),
                user.getEmail(),
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO()
        ));
    }
}
