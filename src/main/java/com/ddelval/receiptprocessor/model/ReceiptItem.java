package com.ddelval.receiptprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptItem {
    private String shortDescription;
    private BigDecimal price;
}