package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// ВНИМАНИЕ: Здесь должно быть написано public INTERFACE, а не class!
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}