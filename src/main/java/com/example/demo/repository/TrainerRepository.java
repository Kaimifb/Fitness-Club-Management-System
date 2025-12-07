package com.example.demo.repository;

import com.example.demo.entity.Trainer;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // <<< Рекомендуется для явной аннотации

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью Trainer (Тренер).
 * Предоставляет методы CRUD и кастомные запросы для поиска тренеров.
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    /**
     * Ищет сущность Тренера, связанную с конкретным объектом Пользователя (User).
     * Этот метод используется при авторизации, чтобы найти профиль тренера по его логину.
     *
     * @param user Объект User, полученный из сессии Spring Security.
     * @return Optional, содержащий найденный объект Trainer, или пустой Optional, если связи нет.
     */
    Optional<Trainer> findByUser(User user);
    Optional<Trainer> findByUser_Username(String username);
}