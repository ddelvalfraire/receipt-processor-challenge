package com.ddelval.receiptprocessor.controller;

import com.ddelval.receiptprocessor.dto.CreateReceiptDTO;
import com.ddelval.receiptprocessor.dto.ReceiptProcessResponse;
import com.ddelval.receiptprocessor.service.ReceiptService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/process")
    public ReceiptProcessResponse processReceipt(@RequestBody CreateReceiptDTO createReceiptDTO) {
        return new ReceiptProcessResponse();
    }

  @GetMapping("/{id}/points")
  public int getPoints(@PathVariable String id) {
        return 0;
  }

}
