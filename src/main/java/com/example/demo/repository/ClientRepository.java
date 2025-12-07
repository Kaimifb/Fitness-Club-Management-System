package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Интерфейс расширяет JpaRepository<Entity, TypeOfId>
@Repository // Добавим явную аннотацию, если ее не было
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Поиск клиента по объекту User (используется редко)
    Optional<Client> findByUser(User user);

    // Поиск по Email (для проверки уникальности)
    Optional<Client> findByEmail(String email);

    /**
     * Поиск клиента по логину связанного пользователя.
     */
    Optional<Client> findByUser_Username(String username);
}

