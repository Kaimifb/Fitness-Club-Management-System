package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // <--- Импорт 1
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // <--- Импорт 2

@SpringBootApplication
// 2. Явно указываем, где искать интерфейсы репозиториев
@EnableJpaRepositories("com.example.demo.repository")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}