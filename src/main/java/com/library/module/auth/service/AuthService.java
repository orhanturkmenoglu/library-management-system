package com.library.module.auth.service;

import com.library.module.auth.dto.request.LoginRequestDTO;
import com.library.module.auth.response.AuthResponse;
import com.library.module.user.dto.UserDTO;

public interface AuthService {

    AuthResponse login(LoginRequestDTO loginRequestDTO);

    AuthResponse signup(UserDTO user);

    void createPasswordResetToken(String email);

    void resetPassword(String token, String newPassword);
}
