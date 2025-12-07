package com.example.demo.config;

import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.entity.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Хешер паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Соединяем ваш сервис и хешер.
    // Spring Boot АВТОМАТИЧЕСКИ найдет этот бин и использует его для логина.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 1. ПУБЛИЧНЫЕ РЕСУРСЫ
                        .requestMatchers("/", "/home", "/css/**", "/js/**", "/login", "/about").permitAll()

                        // 2. АДМИНИСТРАТОР (ROLE_ADMIN) - Полный доступ
                        // Администратор также может управлять клиентами и тренерами через /admin
                        .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())

                        // 3. ТРЕНЕР (ROLE_TRAINER) - Доступ к своему кабинету
                        .requestMatchers("/trainer/**").hasRole(Role.TRAINER.name())

                        // 4. КЛИЕНТ (ROLE_CLIENT) - Доступ к расписанию и профилю
                        // ВАЖНО: Весь функционал клиента (бронирование, профиль) находится здесь!
                        .requestMatchers("/client/**").hasRole(Role.CLIENT.name())

                        // 5. ВСЕ ОСТАЛЬНОЕ
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/home", true)
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}