package com.ddelval.receiptprocessor.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.ddelval.receiptprocessor.dto.ReceiptDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @NotNull
    private UUID id;

    @NotBlank(message = "Retailer is required")
    private String retailer;

    @NotNull(message = "Purchase date is required")
    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate;

    @NotNull(message = "Purchase time is required")
    private LocalTime purchaseTime;

    @NotNull(message = "Total is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total must be greater than or equal to 0")
    @Digits(integer = 10, fraction = 2, message = "Total must have at most 2 decimal places")
    private BigDecimal total;

    @NotEmpty(message = "Items list cannot be empty")
    @Size(min = 1, message = "At least one item is required")
    @Valid
    private List<ReceiptItem> items;

    public Receipt(ReceiptDto receiptDto) {
        this.id = UUID.randomUUID();
        this.retailer = receiptDto.getRetailer();
        this.purchaseDate = receiptDto.getPurchaseDate();
        this.purchaseTime = receiptDto.getPurchaseTime();
        this.total = receiptDto.getTotal();
        this.items = receiptDto.getItems();
    }
}
