package com.library.module.user.dto;

import com.library.shared.domain.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message ="Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String fullName;

    @NotNull(message = "User role is required")
    private UserRole role;

    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must be valid"
    )
    private String phone;

    private LocalDateTime lastLogin;
}
