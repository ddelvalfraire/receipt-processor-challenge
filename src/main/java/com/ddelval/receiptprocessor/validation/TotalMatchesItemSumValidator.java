package com.ddelval.receiptprocessor.validation;

import com.ddelval.receiptprocessor.dto.ReceiptDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TotalMatchesItemSumValidator implements ConstraintValidator<TotalMatchesItemSum, ReceiptDto> {
    @Override
    public boolean isValid(ReceiptDto receipt, ConstraintValidatorContext context) {
        if (receipt.getTotal() == null || receipt.getItems() == null || receipt.getItems().isEmpty()) {
            return true; // Let other validations handle these cases
        }

        BigDecimal itemsSum = receipt.getItems().stream()
                .map(item -> item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return receipt.getTotal().setScale(2, RoundingMode.HALF_UP).equals(itemsSum);
    }
}