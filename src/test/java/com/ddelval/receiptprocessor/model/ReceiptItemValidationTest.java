package com.ddelval.receiptprocessor.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptItemValidationTest {

    private Validator validator;
    private ReceiptItem validItem;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validItem = new ReceiptItem("Mountain Dew", new BigDecimal("1.99"));
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        Set<ConstraintViolation<ReceiptItem>> violations = validator.validate(validItem);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenShortDescriptionIsNull_thenViolation() {
        validItem.setShortDescription(null);
        Set<ConstraintViolation<ReceiptItem>> violations = validator.validate(validItem);

        assertFalse(violations.isEmpty());
        assertEquals("Item description is required", violations.iterator().next().getMessage());
    }

    @Test
    void whenPriceIsNull_thenViolation() {
        validItem.setPrice(null);
        Set<ConstraintViolation<ReceiptItem>> violations = validator.validate(validItem);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Price is required")));
    }

    @Test
    void whenPriceIsNegative_thenViolation() {
        validItem.setPrice(new BigDecimal("-1.00"));
        Set<ConstraintViolation<ReceiptItem>> violations = validator.validate(validItem);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("must be greater than or equal to 0")));
    }

    @Test
    void whenPriceHasMoreThanTwoDecimals_thenViolation() {
        validItem.setPrice(new BigDecimal("1.999"));
        Set<ConstraintViolation<ReceiptItem>> violations = validator.validate(validItem);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("must have at most 2 decimal places")));
    }
}