package com.bridgelabz.quantitymeasurement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class QuantityInputDTO {

    @NotNull(message = "First quantity cannot be null")
    @Valid
    private QuantityDTO thisQuantityDTO;

    @Valid
    private QuantityDTO thatQuantityDTO;

    public QuantityInputDTO() {}

    public QuantityInputDTO(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        this.thisQuantityDTO = thisQuantityDTO;
        this.thatQuantityDTO = thatQuantityDTO;
    }

    public QuantityDTO getThisQuantityDTO() { return thisQuantityDTO; }
    public void setThisQuantityDTO(QuantityDTO thisQuantityDTO) { this.thisQuantityDTO = thisQuantityDTO; }

    public QuantityDTO getThatQuantityDTO() { return thatQuantityDTO; }
    public void setThatQuantityDTO(QuantityDTO thatQuantityDTO) { this.thatQuantityDTO = thatQuantityDTO; }
}
