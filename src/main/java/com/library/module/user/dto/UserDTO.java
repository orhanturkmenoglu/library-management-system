package com.library.module.user.dto;

import com.library.shared.domain.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String email;

    private String fullName;

    private UserRole role;

    private String phone;

    private LocalDateTime lastLogin;
}
