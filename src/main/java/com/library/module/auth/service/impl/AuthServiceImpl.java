package com.library.module.auth.service.impl;

import com.library.module.auth.dto.request.LoginRequestDTO;
import com.library.module.auth.mapper.UserMapper;
import com.library.module.auth.model.PasswordResetToken;
import com.library.module.auth.repository.PasswordResetTokenRepository;
import com.library.module.auth.response.AuthResponse;
import com.library.module.auth.service.AuthService;
import com.library.module.auth.service.CustomUserDetailsService;
import com.library.module.notification.service.EmailService;
import com.library.module.user.dto.UserDTO;
import com.library.module.user.model.User;
import com.library.module.user.repository.UserRepository;
import com.library.shared.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    private static final String FRONTEND_RESET_URL = "http://localhost:5173/reset-password?token=";


    @Override
    @Transactional
    public AuthResponse login(LoginRequestDTO request) {

        Authentication authentication = authenticate(request);
        setSecurityContext(authentication);

        User user = findUserByEmail(request.email());
        updateLastLogin(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return buildAuthResponse(
                "Login Success",
                "Welcome back " + user.getFullName(),
                token,
                user
        );
    }

    private Authentication authenticate(LoginRequestDTO request) {
        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(request.email());

        if (!passwordEncoder.matches(request.password(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );
    }

    @Override
    @Transactional
    public AuthResponse signup(UserDTO userDTO) {

        validateEmailUniqueness(userDTO.getEmail());

        User entity = UserMapper.toEntity(userDTO);
        entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(entity);

        String token = jwtUtil.generateToken(savedUser.getEmail());

        return buildAuthResponse(
                "Registration Successful",
                "User registered successfully",
                token,
                savedUser
        );
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateKeyException("Email address is already in use");
        }
    }

    @Override
    @Transactional
    public void createPasswordResetToken(String email) {

        User user = findUserByEmail(email);
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();

        passwordResetTokenRepository.save(resetToken);

        sendResetEmail(user.getEmail(), token);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid password reset token"));

        validateTokenExpiry(resetToken);

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);

        log.info("Password successfully reset for {}", user.getEmail());
    }

    private void validateTokenExpiry(PasswordResetToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Password reset token has expired");
        }
    }

    private void sendResetEmail(String email, String token) {
        String resetLink = FRONTEND_RESET_URL + token;
        String subject = "Password Reset";
        String body = "Click the link below to reset your password (valid for 5 minutes):\n" + resetLink;

        emailService.sendEmail(email, subject, body);
        log.info("Password reset email sent to {}", email);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private void updateLastLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    private void setSecurityContext(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private AuthResponse buildAuthResponse(
            String title,
            String message,
            String token,
            User user
    ) {
        return AuthResponse.builder()
                .title(title)
                .message(message)
                .token(token)
                .user(UserMapper.toDTO(user))
                .build();
    }
}
