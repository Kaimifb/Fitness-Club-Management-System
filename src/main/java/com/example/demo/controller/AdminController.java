package com.example.demo.controller;

import com.example.demo.dto.ClientRegistrationDto;
import com.example.demo.dto.TrainerRegistrationDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Client;
import com.example.demo.entity.Trainer;
import com.example.demo.entity.TrainingSession;
import com.example.demo.service.BookingService;
import com.example.demo.service.ClientService;
import com.example.demo.service.TrainerService;
import com.example.demo.service.TrainingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final ClientService clientService;
    private final TrainerService trainerService;
    private final TrainingSessionService sessionService;
    private final BookingService bookingService;

    @Autowired
    public AdminController(ClientService clientService,
                           TrainerService trainerService,
                           TrainingSessionService sessionService,
                           BookingService bookingService) {
        this.clientService = clientService;
        this.trainerService = trainerService;
        this.sessionService = sessionService;
        this.bookingService = bookingService;
    }

    // ---------------------- DASHBOARD ----------------------
    @GetMapping
    public String adminPage() {
        return "admin/dashboard";
    }


    // ======================================================
    //                  КЛИЕНТЫ
    // ======================================================

    @GetMapping("/clients")
    public String showClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "admin/clients";
    }

    @GetMapping("/clients/new")
    public String newClient(Model model) {
        model.addAttribute("registrationDto", new ClientRegistrationDto());
        return "admin/client_registration";
    }

    @PostMapping("/clients/register")
    public String registerClient(@ModelAttribute("registrationDto") ClientRegistrationDto dto,
                                 RedirectAttributes redirect) {

        try {
            clientService.registerClient(
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getPhoneNumber(),
                    dto.getEmail(),
                    dto.getUsername(),
                    dto.getPassword()
            );

            redirect.addFlashAttribute("success", "Клиент успешно создан!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/clients";
    }

    @PostMapping("/clients/delete/{id}")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirect) {

        boolean deleted = clientService.deleteClientAndUser(id);

        if (deleted)
            redirect.addFlashAttribute("success", "Клиент удалён.");
        else
            redirect.addFlashAttribute("error", "Клиент не найден.");

        return "redirect:/admin/clients";
    }


    // ======================================================
    //                  ТРЕНЕРЫ
    // ======================================================

    @GetMapping("/trainers")
    public String trainers(Model model) {
        model.addAttribute("trainers", trainerService.findAll());
        return "admin/trainers";
    }

    @GetMapping("/trainers/new")
    public String newTrainer(Model model) {
        model.addAttribute("registrationDto", new TrainerRegistrationDto());
        return "admin/trainer_registration";
    }

    @PostMapping("/trainers/register")
    public String registerTrainer(@ModelAttribute("registrationDto") TrainerRegistrationDto dto,
                                  RedirectAttributes redirect) {

        try {
            trainerService.registerTrainer(
                    dto.getFullName(),
                    dto.getSpecialization(),
                    dto.getUsername(),
                    dto.getPassword()
            );

            redirect.addFlashAttribute("success", "Тренер успешно создан!");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/trainers";
    }

    @PostMapping("/trainers/delete/{id}")
    public String deleteTrainer(@PathVariable Long id,
                                RedirectAttributes redirect) {

        boolean deleted = trainerService.deleteTrainerAndUser(id);

        if (deleted)
            redirect.addFlashAttribute("success", "Тренер удалён.");
        else
            redirect.addFlashAttribute("error", "Тренер не найден.");

        return "redirect:/admin/trainers";
    }


    // ======================================================
    //                  РАСПИСАНИЕ
    // ======================================================

    @GetMapping("/schedule")
    public String showAllSchedule(Model model) {
        List<TrainingSession> sessions = sessionService.findAll();
        model.addAttribute("trainings", sessions);
        return "admin/schedule";
    }

    @GetMapping("/schedule/session/{id}")
    public String viewSessionDetails(@PathVariable Long id, Model model) {

        TrainingSession session = sessionService.findById(id).orElse(null);
        if (session == null) {
            model.addAttribute("error", "Занятие не найдено");
            return "error/404";
        }

        List<Booking> bookings = bookingService.findBySession(session);

        model.addAttribute("training", session);
        model.addAttribute("bookings", bookings);

        return "admin/session_details";
    }

    @PostMapping("/attendance/{id}")
    public String markAttendance(@PathVariable Long id, RedirectAttributes redirect) {

        try {
            bookingService.markAttended(id);
            redirect.addFlashAttribute("success", "Посещение отмечено");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/schedule";
    }

    @PostMapping("/attendance/missed/{id}")
    public String markMissed(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            bookingService.markMissed(id);
            redirect.addFlashAttribute("success", "Отмечено как пропущено");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/schedule";
    }

}
