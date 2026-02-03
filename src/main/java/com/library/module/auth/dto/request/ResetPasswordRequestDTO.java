package com.library.module.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDTO(
        @NotBlank(message = "Token is required")
        String token,
        @NotBlank(message = "Password is required")
        String password
) {
}
