package com.library.module.auth.mapper;

import com.library.module.user.dto.UserDTO;
import com.library.shared.domain.UserRole;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public static UserDTO toDTO(com.library.module.user.model.User user){
       return UserDTO.builder()
               .id(user.getId())
               .fullName(user.getFullName())
               .email(user.getEmail())
               .phone(user.getPhone())
               .lastLogin(user.getLastLogin())
               .role(user.getRole())
               .build();
    }

    public  static com.library.module.user.model.User toEntity(UserDTO userDTO){
        return com.library.module.user.model.User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .role(userDTO.getRole() != null ? userDTO.getRole() : UserRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();
    }
}
