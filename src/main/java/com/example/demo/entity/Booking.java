package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.demo.entity.AttendanceStatus;


/**
 * Сущность Booking (Бронирование).
 * Регистрирует факт записи конкретного клиента на конкретный слот расписания (TrainingSession).
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь: Одно бронирование относится к одной тренировке (ManyToOne)
    // TrainingSession является слотом расписания.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private TrainingSession session; // <<< Оставили только session

    /**
     * Клиент, совершивший бронирование.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /**
     * Время создания записи бронирования (для логов и истории).
     */
    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status = AttendanceStatus.NONE;

    // --- Конструкторы, Геттеры и Сеттеры ---

    public Booking() {
        this.bookingTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public TrainingSession getSession() { // <<< Добавлен геттер для session
        return session;
    }

    public void setSession(TrainingSession session) { // <<< Добавлен сеттер для session
        this.session = session;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }


    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

}