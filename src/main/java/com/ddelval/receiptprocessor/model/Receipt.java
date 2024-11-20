package com.ddelval.receiptprocessor.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.ddelval.receiptprocessor.dto.CreateReceiptDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    private UUID id;
    private String retailer;
    private LocalDate purchaseDate;
    private LocalTime purchaseTime;
    private BigDecimal total;
    private List<ReceiptItem> items;

    public Receipt(CreateReceiptDTO createReceiptDTO) {
        this.id = UUID.randomUUID();
        this.retailer = createReceiptDTO.getRetailer();
        this.purchaseDate = createReceiptDTO.getPurchaseDate();
        this.purchaseTime = createReceiptDTO.getPurchaseTime();
        this.total = createReceiptDTO.getTotal();
        this.items = createReceiptDTO.getItems();
    }
}
