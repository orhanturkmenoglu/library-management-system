package com.library.auth.service;

import com.library.payload.dto.UserDTO;
import com.library.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String username, String password);

    AuthResponse register(UserDTO user);

    void createPasswordResetToken(String email);

    void resetPassword(String token, String newPassword);

}
