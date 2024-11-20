package com.ddelval.receiptprocessor.service;

import com.ddelval.receiptprocessor.dto.ReceiptDto;
import com.ddelval.receiptprocessor.model.Points;
import com.ddelval.receiptprocessor.model.Receipt;
import com.ddelval.receiptprocessor.util.ReceiptPointsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final Map<UUID, Receipt> receiptsMap = new ConcurrentHashMap<>();
    private final Map<UUID, Points> pointsMap = new ConcurrentHashMap<>();

    public Receipt createReceipt(ReceiptDto receiptDto) {
        var receipt = new Receipt(receiptDto);
        receiptsMap.put(receipt.getId(), receipt);
        return receipt;
    }

    public Points fetchReceiptPoints(UUID id) {
       Points points = pointsMap.getOrDefault(id, null);

       if (points != null && !points.isOutdated()) {
           return points;
       }

       Receipt receipt = receiptsMap.getOrDefault(id, null);

       if (receipt == null) {
           return null;
       }

       int pointsValue = ReceiptPointsUtil.calcReceiptPoints(receipt);

       points = new Points(receipt.getId(), pointsValue, false);

       pointsMap.put(id, points);

       return points;
    }
}
