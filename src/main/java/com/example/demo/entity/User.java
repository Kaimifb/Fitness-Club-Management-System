package com.example.demo.entity;

import jakarta.persistence.*;
// Импортируем Role из отдельного файла
import com.example.demo.entity.Role;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String password; // Пароль хранится в хешированном виде

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Роль пользователя (ADMIN, TRAINER, USER)

    // *** СВЯЗИ УДАЛЕНЫ ***
    // Связи @OneToOne с Client и Trainer должны находиться ТОЛЬКО в Client.java и Trainer.java,
    // где они управляют внешним ключом user_id.

    // --- Конструкторы ---
    public User() {}

    // --- Геттеры и Сеттеры ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}