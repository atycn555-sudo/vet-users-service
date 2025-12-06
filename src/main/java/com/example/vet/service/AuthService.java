package com.example.vet.service;

import com.example.vet.dto.RegisterAdminDTO;
import com.example.vet.dto.UserLoginRequest;
import com.example.vet.dto.UserLoginResponse;
import com.example.vet.dto.UserResponse;
import com.example.vet.model.User;
import com.example.vet.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterAdminDTO request) {
        // Validación simple: evitar duplicados por email
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new RuntimeException("El email ya está registrado: " + request.getEmail());
        });

        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setToken("Usuario registrado correctamente: " + user.getEmail());
        return response;
    }

    public UserLoginResponse login(UserLoginRequest request) {
        userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + request.getEmail()));

        UserLoginResponse response = new UserLoginResponse();
        response.setToken("Login exitoso para: " + request.getEmail());
        return response;
    }
}
