package com.library.module.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDTO(
        @NotBlank(message = "Email is required")
        String email) {}
