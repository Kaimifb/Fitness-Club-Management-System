package com.example.demo.entity;

import jakarta.persistence.*;

/**
 * Сущность Trainer (Тренер), представляет инструктора, проводящего тренировки.
 * Связана отношением OneToOne с сущностью User для авторизации.
 */
@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName; // <<< ИСПОЛЬЗУЕМ ЭТО ПОЛЕ

    @Column(name = "specialization", nullable = false)
    private String specialization; // Например: 'Йога', 'Силовые тренировки'

    @Column(name = "phone_number")
    private String phoneNumber;

    // Связь один к одному с User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // --- Конструкторы ---
    public Trainer() {}

    // --- Геттеры и Сеттеры ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ИСПРАВЛЕНО: Удалили name/qualification и оставили только fullName
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}