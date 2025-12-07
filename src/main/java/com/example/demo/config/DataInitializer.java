package com.example.demo.config;

import com.example.demo.entity.User;
// *** ИСПРАВЛЕНО: Импортируем Role из отдельного файла ***
import com.example.demo.entity.Role;
// *********************************************************
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Класс для инициализации базы данных при запуске приложения.
 * Автоматически создает учетную запись администратора, если она еще не существует.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Проверяем, существует ли уже ADMIN
            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User();
                admin.setUsername("admin");
                // Хешируем пароль "password" перед сохранением!
                admin.setPassword(passwordEncoder.encode("password"));
                // *** ИСПРАВЛЕНО: Используем внешний Enum Role ***
                admin.setRole(Role.ADMIN);
                // *************************************************

                userRepository.save(admin);

                System.out.println("--- ADMIN USER CREATED ---");
                System.out.println("Login: admin | Password: password");
            }
        };
    }
}