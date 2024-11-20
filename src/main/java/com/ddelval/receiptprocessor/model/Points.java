package com.ddelval.receiptprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Points {
    private UUID receiptId;
    private int value;
    private boolean needsRecalculation;
}
