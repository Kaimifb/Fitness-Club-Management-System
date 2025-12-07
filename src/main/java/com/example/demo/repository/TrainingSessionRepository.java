package com.example.demo.repository;

import com.example.demo.entity.TrainingSession;
import com.example.demo.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {

    /**
     * Найти все тренировки, запланированные конкретным тренером,
     * отсортированные по времени начала.
     */
    List<TrainingSession> findByTrainerOrderBySessionTimeAsc(Trainer trainer);

    /**
     * Найти все будущие тренировки, отсортированные по времени.
     */
    List<TrainingSession> findBySessionTimeAfterOrderBySessionTimeAsc(LocalDateTime now);

    List<TrainingSession> findAllByOrderBySessionTimeAsc();

}