package com.library.module.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email is required")
        String username,
        @NotBlank(message = "Password is required")
        String password
) {
}