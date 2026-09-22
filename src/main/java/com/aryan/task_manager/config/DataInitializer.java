package com.aryan.task_manager.config;


import com.aryan.task_manager.entity.User;
import com.aryan.task_manager.enums.Role;
import com.aryan.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner createAdmin() {
        return args -> {

            if (!userRepository.existsByEmail("admin@taskflow.com")) {

                User admin = User.builder()
                        .name("TaskFlow Admin")
                        .email("admin@taskflow.com")
                        .password(
                                passwordEncoder.encode("Admin@123")
                        )
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(admin);
            }
        };
    }
}