package com.charlles.coupon_processor.controller;

import com.charlles.coupon_processor.dto.CouponDTO;
import com.charlles.coupon_processor.dto.CouponResponseDTO;
import com.charlles.coupon_processor.repository.CouponRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldCreateCoupon() throws Exception {
        CouponDTO dto = new CouponDTO(
                null,
                "CRM025",
                "create discount",
                new BigDecimal("15.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                true
        );

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("CRM025"))
                .andExpect(jsonPath("$.discountValue").value(15.00))
                .andExpect(header().exists("Location"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidData() throws Exception {
        CouponDTO dto = new CouponDTO(
                null,
                null,
                "description",
                new BigDecimal("10.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                true
        );

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindCouponById() throws Exception {
        CouponDTO dto = new CouponDTO(
                null,
                "FIN025",
                "test find",
                new BigDecimal("20.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                true
        );

        String createResponse = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CouponResponseDTO created = objectMapper.readValue(createResponse, CouponResponseDTO.class);

        mockMvc.perform(get("/coupon/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("FIN025"))
                .andExpect(jsonPath("$.discountValue").value(20.00));
    }

    @Test
    void shouldReturnNotFoundWhenCouponDoesNotExist() throws Exception {
        mockMvc.perform(get("/coupon/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteCoupon() throws Exception {
        CouponDTO dto = new CouponDTO(
                null,
                "DEL025",
                "test delete",
                new BigDecimal("25.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                true
        );

        String createResponse = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CouponResponseDTO created = objectMapper.readValue(createResponse, CouponResponseDTO.class);

        mockMvc.perform(delete("/coupon/" + created.id()))
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldReturnConflictWhenDeletingAlreadyDeletedCoupon() throws Exception {
        CouponDTO dto = new CouponDTO(
                null,
                "TWI025",
                "delete twice",
                new BigDecimal("30.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                true
        );

        String createResponse = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CouponResponseDTO created = objectMapper.readValue(createResponse, CouponResponseDTO.class);

        mockMvc.perform(delete("/coupon/" + created.id()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/coupon/" + created.id()))
                .andExpect(status().isConflict());
    }
}