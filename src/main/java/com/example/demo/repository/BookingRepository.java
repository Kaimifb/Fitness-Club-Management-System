package com.example.demo.repository;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Client;
import com.example.demo.entity.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime; // <<< НУЖЕН
import java.util.List;
import java.util.Optional;

@Repository // <<< Аннотация @Repository была потеряна, добавил
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Проверяет, забронировал ли данный клиент уже это занятие.
     */
    Optional<Booking> findByClientAndSession(Client client, TrainingSession session);

    /**
     * Находит все будущие бронирования для конкретного клиента,
     * отсортированные по времени начала тренировки.
     */
    List<Booking> findByClientAndSession_SessionTimeAfterOrderBySession_SessionTimeAsc(
            Client client, LocalDateTime now);
    /**
     * Находит все бронирования для конкретной тренировки.
     */
    List<Booking> findBySession(TrainingSession session);

    List<Booking> findBySessionOrderByBookingTimeAsc(TrainingSession session);
}
