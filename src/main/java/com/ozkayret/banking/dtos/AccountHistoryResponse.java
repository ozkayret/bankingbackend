package com.ozkayret.banking.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountHistoryResponse(
        Long id,
        String from,
        String to,
        BigDecimal amount,
        LocalDateTime transactionDate,
        String status
) {
}
