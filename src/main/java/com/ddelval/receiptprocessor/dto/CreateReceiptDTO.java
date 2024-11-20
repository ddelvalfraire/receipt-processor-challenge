package com.ddelval.receiptprocessor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import com.ddelval.receiptprocessor.model.ReceiptItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReceiptDTO {
    private String retailer;
    private LocalDate purchaseDate;
    private LocalTime purchaseTime;
    private BigDecimal total;
    private List<ReceiptItem> items;
}
