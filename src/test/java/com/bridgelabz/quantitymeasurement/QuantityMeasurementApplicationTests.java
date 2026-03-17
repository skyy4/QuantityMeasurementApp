package com.bridgelabz.quantitymeasurement;

import com.bridgelabz.quantitymeasurement.dto.QuantityDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityInputDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityMeasurementDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuantityMeasurementApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testCompareIntegration() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCH", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity("/api/v1/quantities/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getResultString());
    }

    @Test
    void testAddIntegration() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCH", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity("/api/v1/quantities/add", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getResultValue());
    }

    @Test
    void testInvalidUnitIntegration() {
        QuantityDTO q1 = new QuantityDTO(1.0, "INVALID_UNIT", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/quantities/compare", input, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
