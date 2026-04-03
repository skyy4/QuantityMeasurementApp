package com.bridgelabz.quantitymeasurement;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class QuantityMeasurementApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    @WithMockUser
    void testCompareIntegration() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCH", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        try {
            mockMvc.perform(post("/api/v1/quantities/compare")
                            .with(Objects.requireNonNull(csrf()))
                            .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                            .content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultString").value("true"))
                    .andExpect(jsonPath("$.error").value(false));
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @WithMockUser
    void testAddIntegration() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCH", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        try {
            mockMvc.perform(post("/api/v1/quantities/add")
                            .with(Objects.requireNonNull(csrf()))
                            .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                            .content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultValue").value(2.0))
                    .andExpect(jsonPath("$.resultUnit").value("FEET"))
                    .andExpect(jsonPath("$.error").value(false));
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @WithMockUser
    void testInvalidUnitIntegration() {
        QuantityDTO q1 = new QuantityDTO(1.0, "INVALID_UNIT", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        try {
            mockMvc.perform(post("/api/v1/quantities/compare")
                            .with(Objects.requireNonNull(csrf()))
                            .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                            .content(Objects.requireNonNull(objectMapper.writeValueAsString(input))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.error").value(true))
                    .andExpect(jsonPath("$.errorMessage").value("Unit 'INVALID_UNIT' is invalid for LengthUnit"));
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

}
