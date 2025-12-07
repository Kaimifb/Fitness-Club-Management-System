package com.example.demo.controller;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Client;
import com.example.demo.entity.TrainingSession;
import com.example.demo.repository.ClientRepository;
import com.example.demo.service.ClientService;
import com.example.demo.service.TrainingSessionService;
import com.example.demo.service.BookingService; // <<< НУЖЕН

// ... (другие импорты: Controller, Model, RequestMapping, и т.д.)

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; // Импорт всех аннотаций, чтобы избежать ошибок
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/client") // <<< Изменил на /client для соответствия логике ролей
public class ClientController {

    private final TrainingSessionService sessionService;
    private final ClientService clientService;
    private final BookingService bookingService;
    private final ClientRepository clientRepository; // Оставил для демонстрационных методов

    // Внедрение зависимости (Dependency Injection) через конструктор
    // ДОБАВЛЕНЫ ВСЕ НУЖНЫЕ СЕРВИСЫ
    @Autowired
    public ClientController(TrainingSessionService sessionService,
                            ClientService clientService,
                            BookingService bookingService,
                            ClientRepository clientRepository) {
        this.sessionService = sessionService;
        this.clientService = clientService;
        this.bookingService = bookingService;
        this.clientRepository = clientRepository;
    }

    // ... (Метод getCurrentClientProfile - оставлен без изменений)
    private Optional<Client> getCurrentClientProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return clientService.findByUser_Username(currentUsername);
    }

    // --- 2. РАСПИСАНИЕ КЛИЕНТА (/client/schedule) ---

    @GetMapping("/schedule")
    public String showPublicSchedule(Model model, RedirectAttributes redirect) {

        Optional<Client> clientOpt = getCurrentClientProfile();

        if (clientOpt.isEmpty()) {
            redirect.addFlashAttribute("error", "Профиль клиента не найден. Перелогиньтесь.");
            return "redirect:/login";
        }

        Client client = clientOpt.get();

        // 1. Список ID всех сессий, на которые клиент записан
        List<Long> bookedSessionIds = bookingService.findUpcomingBookingsByClient(client)
                .stream()
                .map(b -> b.getSession().getId())
                .collect(Collectors.toList());

        // 2. Получаем только корректные, будущие занятия
        List<TrainingSession> sessions =
                sessionService.findAllFutureSessions()
                        .stream()
                        .filter(s -> s.getTrainer() != null)   // защита от null
                        .collect(Collectors.toList());

        // 3. Устанавливаем флаг userBooked для HTML
        for (TrainingSession s : sessions) {
            boolean booked = bookedSessionIds.contains(s.getId());
            s.setUserBooked(booked);
        }

        model.addAttribute("sessions", sessions);
        model.addAttribute("client", client);

        return "client/schedule";
    }


    // --- 3. БРОНИРОВАНИЕ (/client/book/{sessionId}) ---

    @PostMapping("/book/{sessionId}")
    public String bookSession(@PathVariable Long sessionId, RedirectAttributes redirect) {

        Client client = getCurrentClientProfile().orElse(null);
        if (client == null) {
            redirect.addFlashAttribute("error", "Профиль клиента не найден. Перелогиньтесь.");
            return "redirect:/login";
        }

        TrainingSession session = sessionService.findById(sessionId).orElse(null);
        if (session == null) {
            redirect.addFlashAttribute("error", "Тренировка не найдена.");
            return "redirect:/client/schedule";
        }

        try {
            bookingService.bookSession(client, session);
            redirect.addFlashAttribute("success", "Вы успешно записаны на занятие: " + session.getTitle());
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/client/schedule";
    }

    // ------------------------------------------------------------------
    // A. ЛИЧНЫЙ КАБИНЕТ КЛИЕНТА
    // ------------------------------------------------------------------

    /**
     * Отображает личный кабинет клиента, его данные и список предстоящих бронирований.
     */
    @GetMapping("/profile")
    public String showClientProfile(Model model, RedirectAttributes redirect) {

        Client client = getCurrentClientProfile().orElse(null);

        if (client == null) {
            redirect.addFlashAttribute("error", "Профиль клиента не найден. Пожалуйста, перелогиньтесь.");
            return "redirect:/login";
        }

        // Получаем будущие бронирования клиента
        List<Booking> upcomingBookings = bookingService.findUpcomingBookingsByClient(client);

        model.addAttribute("client", client);
        model.addAttribute("bookings", upcomingBookings);

        return "client/profile"; // Шаблон личного кабинета
    }

    // ------------------------------------------------------------------
    // B. ОТМЕНА БРОНИРОВАНИЯ
    // ------------------------------------------------------------------

    /**
     * Обрабатывает запрос на отмену бронирования.
     */
    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId, RedirectAttributes redirect) {

        Client client = getCurrentClientProfile().orElse(null);

        if (client == null) {
            redirect.addFlashAttribute("error", "Ошибка авторизации.");
            return "redirect:/login";
        }

        try {
            // Используем логику отмены, которая уже есть в BookingService
            bookingService.cancelBooking(bookingId, client);
            redirect.addFlashAttribute("success", "Бронирование успешно отменено.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", "Ошибка: Бронирование не найдено.");
        } catch (IllegalStateException e) {
            // Ошибка прав доступа или другая ошибка в логике сервиса
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/client/profile";
    }

}