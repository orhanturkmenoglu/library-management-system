package com.library.shared.config;

import com.library.module.user.model.User;
import com.library.module.user.repository.UserRepository;
import com.library.shared.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.full-name:Admin User}")
    private String adminFullName;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin user already exists. Skipping initialization.");
            return;
        }

        User adminUser = User.builder()
                .fullName(adminFullName)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(UserRole.ROLE_ADMIN)
                .build();

        userRepository.save(adminUser);
        log.info("Admin user successfully created with email: {}", adminEmail);
    }
}
