package com.ddelval.receiptprocessor;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ddelval.receiptprocessor.controller.ReceiptController;
import com.ddelval.receiptprocessor.service.ReceiptService;

@SpringBootTest
class ReceiptProcessorApplicationTests {

    @Autowired
    private ReceiptController receiptController;

    @Autowired
    private ReceiptService receiptService;

     void contextLoads() {
        assertThat(receiptController).isNotNull();
        assertThat(receiptService).isNotNull();
    }


}
