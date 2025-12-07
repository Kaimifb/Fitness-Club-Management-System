package com.example.demo.controller;

import com.example.demo.entity.Trainer;
import com.example.demo.entity.TrainingSession;
import com.example.demo.service.TrainerService;
import com.example.demo.service.TrainingSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainingSessionService sessionService;

    @Autowired
    public TrainerController(TrainerService trainerService,
                             TrainingSessionService sessionService) {
        this.trainerService = trainerService;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Trainer> trainerOpt = trainerService.findByUser_Username(username);

        if (trainerOpt.isEmpty()) {
            model.addAttribute("error", "Профиль тренера не найден!");
            return "error/access-denied";
        }

        model.addAttribute("trainer", trainerOpt.get());
        return "trainer/dashboard";
    }

    @GetMapping("/schedule")
    public String schedule(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Trainer> trainerOpt = trainerService.findByUser_Username(username);

        if (trainerOpt.isEmpty()) {
            model.addAttribute("error", "Профиль тренера не найден");
            return "error/access-denied";
        }

        Trainer trainer = trainerOpt.get();
        List<TrainingSession> sessions = sessionService.findByTrainer(trainer);

        model.addAttribute("sessions", sessions);

        return "trainer/schedule";
    }

    @GetMapping("/sessions/new")
    public String newSessionForm(Model model) {

        model.addAttribute("session", new TrainingSession());
        return "trainer/session_form";
    }

    @PostMapping("/sessions/create")
    public String createSession(@ModelAttribute("session") TrainingSession session,
                                Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Trainer> trainerOpt = trainerService.findByUser_Username(username);
        if (trainerOpt.isEmpty()) {
            model.addAttribute("error", "Профиль тренера не найден");
            return "error/access-denied";
        }

        Trainer trainer = trainerOpt.get();
        session.setTrainer(trainer);

        sessionService.save(session);

        return "redirect:/trainer/schedule";
    }

    @GetMapping("/sessions/edit/{id}")
    public String editSessionForm(@PathVariable Long id, Model model) {

        Optional<TrainingSession> sessionOpt = sessionService.findById(id);
        if (sessionOpt.isEmpty()) {
            model.addAttribute("error", "Занятие не найдено");
            return "error/404";
        }

        model.addAttribute("session", sessionOpt.get());
        return "trainer/session_form";
    }

    @PostMapping("/sessions/update/{id}")
    public String updateSession(@PathVariable Long id,
                                @ModelAttribute("session") TrainingSession session) {

        session.setId(id);
        sessionService.save(session);

        return "redirect:/trainer/schedule";
    }

    @PostMapping("/sessions/delete/{id}")
    public String deleteSession(@PathVariable Long id) {

        sessionService.deleteById(id);
        return "redirect:/trainer/schedule";
    }
}
