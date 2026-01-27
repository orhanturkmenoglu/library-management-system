package com.library.module.auth.service.impl;

import com.library.module.auth.response.AuthResponse;
import com.library.module.auth.service.AuthService;
import com.library.module.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse login(String username, String password) {
        return null;
    }

    @Override
    public AuthResponse register(UserDTO user) {
        return null;
    }

    @Override
    public void createPasswordResetToken(String email) {
    }

    @Override
    public void resetPassword(String token, String newPassword) {
    }
}
