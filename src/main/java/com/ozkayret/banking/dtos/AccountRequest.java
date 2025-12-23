package com.ozkayret.banking.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountRequest(
        @NotNull(message = "number is required")
        @NotBlank(message = "number is required")
        @NotEmpty(message = "number is required")
        String number,
        @NotNull(message = "name is required")
        @NotBlank(message = "name is required")
        @NotEmpty(message = "name is required")
        String name,
        @NotNull(message = "balance is required")
        @NotBlank(message = "balance is required")
        @NotEmpty(message = "balance is required")
        @JsonFormat(shape=JsonFormat.Shape.STRING)
        BigDecimal balance
) {
}
