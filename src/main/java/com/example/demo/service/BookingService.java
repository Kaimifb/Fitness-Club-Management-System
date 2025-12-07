package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Client;
import com.example.demo.entity.TrainingSession;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.TrainingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.AttendanceStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainingSessionRepository sessionRepository; // Нужен для обновления счетчика

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          TrainingSessionRepository sessionRepository) {
        this.bookingRepository = bookingRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Находит все будущие бронирования клиента.
     */
    public List<Booking> findUpcomingBookingsByClient(Client client) {
        return bookingRepository.findByClientAndSession_SessionTimeAfterOrderBySession_SessionTimeAsc(
                client, LocalDateTime.now());
    }

    public List<Booking> findBySession(TrainingSession session) {
        return bookingRepository.findBySessionOrderByBookingTimeAsc(session);
    }



    /**
     * Логика бронирования: создает запись и обновляет счетчик участников.
     */
    @Transactional
    public Booking bookSession(Client client, TrainingSession session) throws IllegalStateException {

        // 1. Проверка на дубликат
        if (bookingRepository.findByClientAndSession(client, session).isPresent()) {
            throw new IllegalStateException("Вы уже записаны на эту тренировку.");
        }

        // 2. Проверка на заполненность
        if (session.getCurrentParticipants() >= session.getMaxParticipants()) {
            throw new IllegalStateException("В группе нет свободных мест.");
        }

        // 3. Создание бронирования
        Booking newBooking = new Booking();
        newBooking.setClient(client);
        newBooking.setSession(session);

        bookingRepository.save(newBooking);

        // 4. Обновление счетчика в TrainingSession
        session.setCurrentParticipants(session.getCurrentParticipants() + 1);
        sessionRepository.save(session); // Сохраняем обновленный счетчик

        return newBooking;
    }

    /**
     * Логика отмены бронирования: удаляет запись и уменьшает счетчик участников.
     */
    @Transactional
    public void cancelBooking(Long bookingId, Client client) throws IllegalStateException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено."));

        // Проверка прав: клиент может отменить только своё бронирование
        if (!booking.getClient().getId().equals(client.getId())) {
            throw new IllegalStateException("У вас нет прав отменить это бронирование.");
        }

        TrainingSession session = booking.getSession();

        // Обновление счетчика
        session.setCurrentParticipants(session.getCurrentParticipants() - 1);
        sessionRepository.save(session);

        // Удаление бронирования
        bookingRepository.delete(booking);
    }

    // ---------------------------------------------------------
    // Отметить посещение
    // ---------------------------------------------------------
    @Transactional
    public void markAttended(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено"));

        booking.setStatus(AttendanceStatus.ATTENDED);
        bookingRepository.save(booking);
    }

    @Transactional
    public void markMissed(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено"));

        booking.setStatus(AttendanceStatus.MISSED);
        bookingRepository.save(booking);
    }

}