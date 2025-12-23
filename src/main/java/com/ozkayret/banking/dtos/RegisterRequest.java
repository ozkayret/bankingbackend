package com.ozkayret.banking.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotNull(message = "username is required")
        @NotBlank(message = "username is required")
        @NotEmpty(message = "username is required")
        String username,
        @NotNull(message = "password is required")
        @NotBlank(message = "password is required")
        @NotEmpty(message = "password is required")
        String password,
        @NotNull(message = "email is required")
        @NotBlank(message = "email is required")
        @NotEmpty(message = "email is required")
        String email,
        @NotNull(message = "name is required")
        @NotBlank(message = "name is required")
        @NotEmpty(message = "name is required")
        String name,
        @NotNull(message = "surname is required")
        @NotBlank(message = "surname is required")
        @NotEmpty(message = "surname is required")
        String surname
) {
}
