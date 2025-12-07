package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

// ... (добавьте другие нужные импорты для валидации: Email, Size и т.д.)

public class TrainerRegistrationDto {
    @NotBlank(message = "Логин не может быть пустым")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @NotBlank(message = "ФИО не может быть пустым")
    private String fullName;

    @NotBlank(message = "Специализация не может быть пустой")
    private String specialization;

    // ... (добавьте поля для email, телефона, если нужно)

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}