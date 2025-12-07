package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

// *** ДОБАВЛЕННЫЕ ИМПОРТЫ ***
import org.springframework.ui.Model; // <<< КРИТИЧЕСКИ ВАЖНЫЙ ИМПОРТ ДЛЯ profilePage
import java.util.List; // <<< Необходим для List<Booking>
// *****************************

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Главная страница (редирект)
    @GetMapping("/")
    public String rootPage() {
        return "redirect:/home";
    }

    // 2. Страница входа (КРИТИЧЕСКИ ВАЖНО)
    @GetMapping("/login")
    public String login() {
        return "security/login"; // Теперь ищет templates/security/login.html
    }

    // 3. Домашняя страница (после входа)
    @GetMapping("/home")
    public String homePage() {
        return "home"; // Возвращает шаблон home.html
    }

    /**
     * Отображает статическую страницу "О нас" с информацией о фитнес-клубе.
     * Доступна всем пользователям (permitAll в SecurityConfig).
     * @return Шаблон 'about.html'.
     */
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}