package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.QuantityInputDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    @Autowired
    private IQuantityMeasurementService service;

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public ResponseEntity<QuantityMeasurementDTO> compareQuantities(@RequestBody QuantityInputDTO input) {
        return new ResponseEntity<>(service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO()),
                HttpStatus.OK);
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to a different unit")
    public ResponseEntity<QuantityMeasurementDTO> convertQuantity(@RequestBody QuantityInputDTO input) {
        return new ResponseEntity<>(service.convert(input.getThisQuantityDTO(), input.getThisQuantityDTO().getUnit()),
                HttpStatus.OK);
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(@RequestBody QuantityInputDTO input) {
        return new ResponseEntity<>(service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO()), HttpStatus.OK);
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(@RequestBody QuantityInputDTO input) {
        return new ResponseEntity<>(service.subtract(input.getThisQuantityDTO(), input.getThatQuantityDTO()),
                HttpStatus.OK);
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(@RequestBody QuantityInputDTO input) {
        return new ResponseEntity<>(service.divide(input.getThisQuantityDTO(), input.getThatQuantityDTO()),
                HttpStatus.OK);
    }

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get measurement history by operation type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(@PathVariable String operation) {
        return new ResponseEntity<>(service.getOperationHistory(operation), HttpStatus.OK);
    }

    @GetMapping("/history/type/{type}")
    @Operation(summary = "Get measurement history by measurement type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementsByType(@PathVariable String type) {
        return new ResponseEntity<>(service.getMeasurementsByType(type), HttpStatus.OK);
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get count of operations by type")
    public ResponseEntity<Long> getOperationCount(@PathVariable String operation) {
        return new ResponseEntity<>(service.getOperationCount(operation), HttpStatus.OK);
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get error history")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        return new ResponseEntity<>(service.getErrorHistory(), HttpStatus.OK);
    }
}
