package com.ozkayret.banking.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "balance is required")
        @NotBlank(message = "balance is required")
        @NotEmpty(message = "balance is required")
        String from,
        @NotNull(message = "balance is required")
        @NotBlank(message = "balance is required")
        @NotEmpty(message = "balance is required")
        String to,
        @NotNull(message = "amount is required")
        @Positive(message = "amount must be positive")
        @JsonFormat(shape= JsonFormat.Shape.STRING)
        BigDecimal amount
) {
}
