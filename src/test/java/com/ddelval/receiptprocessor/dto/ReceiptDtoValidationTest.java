package com.ddelval.receiptprocessor.dto;

import com.ddelval.receiptprocessor.model.ReceiptItem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptDtoValidationTest {

    private Validator validator;
    private ReceiptDto validDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Set up a valid DTO for testing
        validDto = new ReceiptDto();
        validDto.setRetailer("Target");
        validDto.setPurchaseDate(LocalDate.now());
        validDto.setPurchaseTime(LocalTime.of(13, 30));
        validDto.setTotal(new BigDecimal("35.35"));
        validDto.setItems(List.of(
                new ReceiptItem("Item 1", new BigDecimal("15.35")),
                new ReceiptItem("Item 2", new BigDecimal("20.00"))
        ));
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        Set<ConstraintViolation<ReceiptDto>> violations = validator.validate(validDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenRetailerIsNull_thenViolation() {
        validDto.setRetailer(null);
        Set<ConstraintViolation<ReceiptDto>> violations = validator.validate(validDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Retailer is required", violations.iterator().next().getMessage());
    }

    @Test
    void whenPurchaseDateIsInFuture_thenViolation() {
        validDto.setPurchaseDate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<ReceiptDto>> violations = validator.validate(validDto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Purchase date cannot be in the future")));
    }

    @Test
    void whenTotalHasMoreThanTwoDecimals_thenViolation() {
        validDto.setTotal(new BigDecimal("10.999"));
        Set<ConstraintViolation<ReceiptDto>> violations = validator.validate(validDto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("must have at most 2 decimal places")));
    }

    @Test
    void whenItemsListIsEmpty_thenViolation() {
        validDto.setItems(new ArrayList<>());
        Set<ConstraintViolation<ReceiptDto>> violations = validator.validate(validDto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("At least one item is required")));
    }
}