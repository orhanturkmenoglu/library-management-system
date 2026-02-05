package com.library.module.auth.mapper;

import com.library.module.user.dto.UserDTO;
import com.library.module.user.model.User;
import com.library.shared.domain.UserRole;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public static UserDTO toDTO(User user){
       return UserDTO.builder()
               .id(user.getId())
               .fullName(user.getFullName())
               .email(user.getEmail())
               .phone(user.getPhone())
               .lastLogin(user.getLastLogin())
               .role(user.getRole())
               .build();
    }

    public  static User toEntity(UserDTO userDTO){
        return User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .role(userDTO.getRole() != null ? userDTO.getRole() : UserRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();
    }
}
