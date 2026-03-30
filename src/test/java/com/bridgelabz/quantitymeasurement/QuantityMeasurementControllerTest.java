package com.bridgelabz.quantitymeasurement;

import com.bridgelabz.quantitymeasurement.controller.QuantityMeasurementController;
import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityInputDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Objects;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuantityMeasurementController.class)
public class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQuantityMeasurementService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void compareQuantities_Success() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCH", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);
        
        QuantityMeasurementDTO result = QuantityMeasurementDTO.builder()
                .thisValue(1.0).thisUnit("FEET").thisMeasurementType("LengthUnit")
                .thatValue(12.0).thatUnit("INCH").thatMeasurementType("LengthUnit")
                .operation("compare").resultString("true").build();

        Mockito.when(service.compare(Mockito.any(), Mockito.any())).thenReturn(result);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .with(Objects.requireNonNull(csrf()))
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
    @WithMockUser
    public void addQuantities_Success() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCH", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        QuantityMeasurementDTO result = QuantityMeasurementDTO.builder()
                .thisValue(1.0).thisUnit("FEET").thisMeasurementType("LengthUnit")
                .thatValue(12.0).thatUnit("INCH").thatMeasurementType("LengthUnit")
                .operation("add").resultValue(2.0).resultUnit("FEET").build();

        Mockito.when(service.add(Mockito.any(), Mockito.any())).thenReturn(result);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .with(Objects.requireNonNull(csrf()))
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(2.0));
    }

    @Test
    @WithMockUser
    public void getHistory_Success() throws Exception {
        Mockito.when(service.getOperationHistory("compare")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/quantities/history/operation/compare"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON)));
    }
}
