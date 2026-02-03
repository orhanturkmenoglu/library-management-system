package com.library.module.auth.controller;

import com.library.module.auth.dto.request.ForgotPasswordRequestDTO;
import com.library.module.auth.dto.request.LoginRequestDTO;
import com.library.module.auth.dto.request.ResetPasswordRequestDTO;
import com.library.module.auth.response.AuthResponse;
import com.library.module.auth.service.AuthService;
import com.library.module.user.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody  UserDTO userDTO) {
        AuthResponse response = authService.signup(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        AuthResponse response = authService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    // FORGOT PASSWORD (RESET LINK)
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody
                                               @Valid ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        authService.createPasswordResetToken(forgotPasswordRequestDTO.email());
        return ResponseEntity.ok().build();
    }

    // RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequestDTO) {

        authService.resetPassword(resetPasswordRequestDTO.token(),
                resetPasswordRequestDTO.password());
        return ResponseEntity.ok().build();
    }
}
