package com.ddelval.receiptprocessor.service;

import com.ddelval.receiptprocessor.dto.ReceiptDto;
import com.ddelval.receiptprocessor.model.Points;
import com.ddelval.receiptprocessor.model.Receipt;
import com.ddelval.receiptprocessor.model.ReceiptItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReceiptServiceTest {

    @Autowired
    private ReceiptService receiptService;

    private ReceiptDto validReceiptDTO;

    @BeforeEach
    void setUp() {
        validReceiptDTO = new ReceiptDto();
        validReceiptDTO.setRetailer("Target");
        validReceiptDTO.setPurchaseDate(LocalDate.of(2024, 1, 1));
        validReceiptDTO.setPurchaseTime(LocalTime.of(14, 30));
        validReceiptDTO.setTotal(new BigDecimal("35.35"));
        validReceiptDTO.setItems(Arrays.asList(
                new ReceiptItem("Item 1", new BigDecimal("10.00")),
                new ReceiptItem("Item 2", new BigDecimal("25.35"))
        ));
    }

    @Test
    void testCreateReceipt_WithValidCreateReceiptDTO_ShouldReturnReceipt() {
        Receipt createdReceipt = receiptService.createReceipt(validReceiptDTO);


        assertNotNull(createdReceipt);
        assertNotNull(createdReceipt.getId());
        assertEquals(validReceiptDTO.getRetailer(), createdReceipt.getRetailer());
        assertEquals(validReceiptDTO.getPurchaseDate(), createdReceipt.getPurchaseDate());
        assertEquals(validReceiptDTO.getPurchaseTime(), createdReceipt.getPurchaseTime());
        assertEquals(validReceiptDTO.getTotal(), createdReceipt.getTotal());
        assertEquals(validReceiptDTO.getItems(), createdReceipt.getItems());
    }

    @Test
    void testCreateReceipt_WithNullCreateReceiptDTO_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> receiptService.createReceipt(null));
    }

    @Test
    void testFetchReceiptPoints_WithValidId_ShouldReturnReceiptPoints() {
        Receipt createdReceipt = receiptService.createReceipt(validReceiptDTO);

        Points points = receiptService.fetchReceiptPoints(createdReceipt.getId());

        assertNotNull(points);
        assertEquals(createdReceipt.getId(), points.getReceiptId());
        assertTrue(points.getValue() >= 0);
        assertFalse(points.isOutdated());
    }

    @Test
    void testFetchReceiptPoints_WithInvalidId_ShouldReturnNullReceiptPoints() {
        UUID invalidId = UUID.randomUUID();

        Points points = receiptService.fetchReceiptPoints(invalidId);

        assertNull(points);
    }

    @Test
    void testFetchReceiptPoints_WithSameId_ShouldCachePoints() {
        Receipt createdReceipt = receiptService.createReceipt(validReceiptDTO);

        Points firstFetch = receiptService.fetchReceiptPoints(createdReceipt.getId());
        Points secondFetch = receiptService.fetchReceiptPoints(createdReceipt.getId());

        assertNotNull(firstFetch);
        assertNotNull(secondFetch);
        assertEquals(firstFetch.getValue(), secondFetch.getValue());
        assertSame(firstFetch, secondFetch);
    }
}