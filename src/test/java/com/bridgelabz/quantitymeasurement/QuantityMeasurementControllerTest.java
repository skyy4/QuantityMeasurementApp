package com.bridgelabz.quantitymeasurement;

import com.bridgelabz.quantitymeasurement.controller.QuantityMeasurementController;
import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityInputDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class QuantityMeasurementControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private IQuantityMeasurementService service;

    @InjectMocks
    private QuantityMeasurementController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
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
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
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
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(2.0));
    }

    @Test
    public void getHistory_Success() throws Exception {
        Mockito.when(service.getOperationHistory("compare")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/quantities/history/operation/compare"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON)));
    }
}
