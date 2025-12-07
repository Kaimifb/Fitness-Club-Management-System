package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.Trainer;
import com.example.demo.entity.User;
import com.example.demo.repository.TrainerRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // ← ДОБАВЛЕНО

    @Autowired
    public TrainerService(TrainerRepository trainerRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {  // ← ДОБАВЛЕНО
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // ← ДОБАВЛЕНО
    }

    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    public Optional<Trainer> findById(Long id) {
        return trainerRepository.findById(id);
    }

    public Optional<Trainer> findByUser(User user) {
        return trainerRepository.findByUser(user);
    }

    public Optional<Trainer> findByUser_Username(String username) {
        return trainerRepository.findByUser_Username(username);
    }

    public Trainer save(Trainer trainer) {
        return trainerRepository.save(trainer);
    }


    // ==========================================================
    //            РЕГИСТРАЦИЯ НОВОГО ТРЕНЕРА
    // ==========================================================
    public Trainer registerTrainer(String fullName,
                                   String specialization,
                                   String username,
                                   String rawPassword) {

        // Проверка что логин ещё не существует
        if (userRepository.findByUsername(username).isPresent())
            throw new IllegalStateException("Логин уже существует");

        // Создаём User
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword)); // ← работает
        user.setRole(Role.TRAINER);
        userRepository.save(user);

        // Создаём Trainer
        Trainer t = new Trainer();
        t.setFullName(fullName);
        t.setSpecialization(specialization);
        t.setUser(user);

        return trainerRepository.save(t);
    }

    // ==========================================================
    //            УДАЛЕНИЕ ТРЕНЕРА + USER
    // ==========================================================
    @Transactional
    public boolean deleteTrainerAndUser(Long trainerId) {

        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer == null)
            return false;

        User user = trainer.getUser();

        trainerRepository.delete(trainer);

        if (user != null)
            userRepository.delete(user);

        return true;
    }
}
