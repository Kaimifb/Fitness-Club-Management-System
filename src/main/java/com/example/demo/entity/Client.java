package com.example.demo.entity;

import jakarta.persistence.*;
/**
 * Сущность Client (Клиент), представляющая реального посетителя фитнес-клуба.
 */
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // СВЯЗЬ: OneToOne к User. Эта таблица (clients) хранит внешний ключ user_id.
    @OneToOne(fetch = FetchType.LAZY) // Добавим LAZY
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    // Вместо firstName/lastName используем одно поле для ФИО
    @Column(name = "full_name", nullable = false)
    private String fullName; // <<< ИСПРАВЛЕНО

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    @Override
    public String toString() {
        return fullName;
    }



    // --- КОНСТРУКТОРЫ ---
    public Client() {
    }

    // --- Геттеры и Сеттеры (Обновлены) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // ИСПРАВЛЕНО
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // УДАЛЕНЫ getFirstName, setFirstName, getLastName, setLastName

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}