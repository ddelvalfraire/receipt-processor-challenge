package com.ddelval.receiptprocessor.controller;

import com.ddelval.receiptprocessor.dto.ReceiptDto;
import com.ddelval.receiptprocessor.model.Points;
import com.ddelval.receiptprocessor.model.Receipt;
import com.ddelval.receiptprocessor.model.ReceiptItem;
import com.ddelval.receiptprocessor.service.ReceiptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReceiptController.class)
class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceiptService receiptService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReceiptDto validReceiptDTO;
    private Receipt mockReceipt;
    private UUID validUUID;

    @BeforeEach
    void setUp() {
        validUUID = UUID.randomUUID();

        // Create valid DTO
        validReceiptDTO = new ReceiptDto();
        validReceiptDTO.setRetailer("Target");
        validReceiptDTO.setPurchaseDate(LocalDate.of(2024, 1, 1));
        validReceiptDTO.setPurchaseTime(LocalTime.of(14, 30));
        validReceiptDTO.setTotal(new BigDecimal("35.35"));
        validReceiptDTO.setItems(Arrays.asList(
                new ReceiptItem("Item 1", new BigDecimal("10.00")),
                new ReceiptItem("Item 2", new BigDecimal("25.35"))
        ));

        // Create mock receipt
        mockReceipt = new Receipt(validReceiptDTO);
        mockReceipt.setId(validUUID);
    }

    @Test
    void processReceipt_WithValidData_ShouldReturnOkAndId() throws Exception {
        when(receiptService.createReceipt(any(ReceiptDto.class))).thenReturn(mockReceipt);

        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReceiptDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validUUID.toString()));
    }

    @Test
    void processReceipt_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        ReceiptDto invalidDTO = new ReceiptDto();

        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPoints_WithValidUUID_ShouldReturnPointsResponse() throws Exception {
        Points mockPoints = new Points(validUUID, 100, false);
        when(receiptService.fetchReceiptPoints(validUUID)).thenReturn(mockPoints);

        mockMvc.perform(get("/receipts/{id}/points", validUUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(100));
    }

    @Test
    void getPoints_WithInvalidUUID_ShouldReturnNotFound() throws Exception {
        when(receiptService.fetchReceiptPoints(any(UUID.class))).thenReturn(null);

        mockMvc.perform(get("/receipts/{id}/points", validUUID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPoints_WithMalformedUUID_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/receipts/{id}/points", "not-a-uuid"))
                .andExpect(status().isBadRequest());
    }
}