package com.ozkayret.banking.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountDetailResponse(
        UUID id,
        String number,
        String name,
        BigDecimal balance) {
}
