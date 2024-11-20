package com.ddelval.receiptprocessor.service;

import com.ddelval.receiptprocessor.dto.CreateReceiptDTO;
import com.ddelval.receiptprocessor.model.Points;
import com.ddelval.receiptprocessor.model.Receipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final Map<UUID, Receipt> receipts = new ConcurrentHashMap<>();
    private final Map<UUID, Point> points = new ConcurrentHashMap<>();

    public Receipt createReceipt(CreateReceiptDTO createReceiptDTO) {
        var receipt = new Receipt(createReceiptDTO);
        receipts.put(receipt.getId(), receipt);
        return receipt;
    }

    public Points fetchReceiptPoints(UUID id) {
        return new Points(UUID.randomUUID(), 0, false);
    }

    private Points calculatePoints(Receipt receipt) {
        return new Points(UUID.randomUUID(), 0, false);
    }
}
