package com.ddelval.receiptprocessor.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.ddelval.receiptprocessor.dto.ReceiptDto;
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

    public Receipt(ReceiptDto receiptDto) {
        this.id = UUID.randomUUID();
        this.retailer = receiptDto.getRetailer();
        this.purchaseDate = receiptDto.getPurchaseDate();
        this.purchaseTime = receiptDto.getPurchaseTime();
        this.total = receiptDto.getTotal();
        this.items = receiptDto.getItems();
    }
}
