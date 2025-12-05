package com.example.vet.controller;

import com.example.vet.model.User;
import com.example.vet.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Obtener nombre por ID
    @GetMapping("/{userId}/name")
    public String getUserName(@PathVariable Integer userId) {
        return userRepository.findById(userId)
                .map(User::getNombre)
                .orElse("Usuario no encontrado");
    }

    // ✅ Listar todos los usuarios
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Buscar usuario por email (útil para Feign Client)
    @GetMapping("/by-email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }
}
