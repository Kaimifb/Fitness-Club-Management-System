package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.entity.User; // <<< Убедитесь, что User импортирован
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.UserRepository; // <<< Нужен для удаления User
import com.example.demo.entity.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <<< КРИТИЧЕСКИ ВАЖНО

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления бизнес-логикой, связанной с сущностью Client.
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Client registerClient(String firstName, String lastName,
                                 String phoneNumber, String email,
                                 String username, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Логин уже существует: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.CLIENT);
        userRepository.save(user);

        Client client = new Client();
        client.setFullName(firstName + " " + lastName);
        client.setPhoneNumber(phoneNumber);
        client.setEmail(email);
        client.setUser(user);

        return clientRepository.save(client);
    }

    public Optional<Client> findByUser_Username(String username) {
        return clientRepository.findByUser_Username(username);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional
    public boolean deleteClientAndUser(Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) return false;

        Client client = clientOpt.get();
        User user = client.getUser();

        clientRepository.delete(client);
        if (user != null) userRepository.delete(user);

        return true;
    }
}
