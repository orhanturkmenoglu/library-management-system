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
import com.library.shared.domain.UserRole;
import com.library.shared.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public AuthResponse login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication =
                authenticate(loginRequestDTO);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(loginRequestDTO.username());

        User user = userRepository.findByEmail(loginRequestDTO.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found "));

        user.setLastLogin(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                .title("Login success")
                .message("Welcome Back " + savedUser.getFullName())
                .token(token)
                .user(UserMapper.toDTO(savedUser))
                .build();
    }

    private Authentication authenticate(LoginRequestDTO loginRequestDTO) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequestDTO.username());

        if (userDetails == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        if (!passwordEncoder.matches(loginRequestDTO.password(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(loginRequestDTO.username(),
                null,
                userDetails.getAuthorities());
    }

    @Transactional
    @Override
    public AuthResponse signup(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        User newUser = User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .role(UserRole.ROLE_USER)
                .profileImage(user.getProfileImage())
                .lastLogin(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),
                savedUser.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = jwtUtil.generateToken(savedUser.getEmail());

        return AuthResponse.builder()
                .token(accessToken)
                .user(UserMapper.toDTO(savedUser))
                .message("User registered successfully")
                .title("Registration Successful")
                .build();
    }

    @Transactional
    @Override
    public void createPasswordResetToken(String email) {

        // email ile sıfırlama linki gönder
        String frontendUrl = "http://localhost:5173";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();


        passwordResetTokenRepository.save(passwordResetToken);
        String resetLink = frontendUrl + token;
        String subject = "Password Reset Link";
        String body = "You requested to reset your password.Use this link (valid 3 minutes) : {}" + resetLink;

        emailService.sendEmail(user.getEmail(), subject, body);
        log.info("Email sent to {}", resetLink);
    }

    @Transactional
    @Override
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Password reset token is invalid"));

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Password reset token has expired");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);

        log.info("Password reset token has been saved to {}", user.getEmail());
    }
}
