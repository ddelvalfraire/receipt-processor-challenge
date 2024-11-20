package com.ddelval.receiptprocessor.controller;

import com.ddelval.receiptprocessor.dto.ReceiptDto;
import com.ddelval.receiptprocessor.dto.GetPointsResponse;
import com.ddelval.receiptprocessor.dto.ProcessReceiptResponse;
import com.ddelval.receiptprocessor.model.Points;
import com.ddelval.receiptprocessor.model.Receipt;
import com.ddelval.receiptprocessor.service.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/process")
    public ResponseEntity<ProcessReceiptResponse> processReceipt(@Valid @RequestBody ReceiptDto receiptDto) {
        Receipt receipt = receiptService.createReceipt(receiptDto);
        return ResponseEntity.ok(new ProcessReceiptResponse(receipt.getId()));
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<GetPointsResponse> getPoints(@PathVariable UUID id) {
        Points points = receiptService.fetchReceiptPoints(id);
        if (points == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GetPointsResponse(points.getValue()));
    }

}
