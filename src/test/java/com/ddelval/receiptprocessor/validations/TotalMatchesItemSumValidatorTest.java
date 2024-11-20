package com.ddelval.receiptprocessor.validations;

import com.ddelval.receiptprocessor.dto.ReceiptDto;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TotalMatchesItemSumValidatorTest {

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
    void whenTotalMatchesItemSum_thenNoViolations() {
        Set<ConstraintViolation<ReceiptDto>> violations = validator.validate(validDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenTotalDoesNotMatchItemSum_thenViolation() {
        validDto.setTotal(new BigDecimal("35.36")); // Off by one cent
        Set<ConstraintViolation<ReceiptDto>> violations = validator.validate(validDto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Total must match sum of item prices")));
    }

    @Test
    void whenItemsAreNull_thenSkipsValidation() {
        validDto.setItems(null);
        Set<ConstraintViolation<ReceiptDto>> violations = validator.validate(validDto);

        // Should only have the @NotEmpty violation, not our custom one
        assertTrue(violations.stream()
                .noneMatch(v -> v.getMessage().equals("Total must match sum of item prices")));
    }

    @Test
    void whenTotalIsNull_thenSkipsValidation() {
        validDto.setTotal(null);
        Set<ConstraintViolation<ReceiptDto>> violations = validator.validate(validDto);

        // Should only have the @NotNull violation, not our custom one
        assertTrue(violations.stream()
                .noneMatch(v -> v.getMessage().equals("Total must match sum of item prices")));
    }
}