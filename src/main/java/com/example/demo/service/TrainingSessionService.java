package com.example.demo.service;

import com.example.demo.entity.Trainer;
import com.example.demo.entity.TrainingSession;
import com.example.demo.repository.TrainingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingSessionService {

    private final TrainingSessionRepository sessionRepository;

    @Autowired
    public TrainingSessionService(TrainingSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<TrainingSession> findAllFutureSessions() {
        return sessionRepository.findBySessionTimeAfterOrderBySessionTimeAsc(LocalDateTime.now());
    }

    public Optional<TrainingSession> findById(Long id) {
        return sessionRepository.findById(id);
    }

    /**
     * Сохраняет новую тренировку или обновляет существующую.
     */
    public TrainingSession save(TrainingSession session) {
        return sessionRepository.save(session);
    }

    public void deleteById(Long id) {
        sessionRepository.deleteById(id);
    }

    /**
     * Находит расписание, созданное конкретным тренером.
     */
    public List<TrainingSession> findByTrainer(Trainer trainer) {
        return sessionRepository.findByTrainerOrderBySessionTimeAsc(trainer);
    }

    public List<TrainingSession> findAll() {
        return sessionRepository.findAllByOrderBySessionTimeAsc();
    }

}