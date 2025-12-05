package com.example.vet.service;

import com.example.vet.dto.RegisterAdminDTO;
import com.example.vet.dto.UserLoginRequest;
import com.example.vet.dto.UserResponse;
import com.example.vet.model.User;
import com.example.vet.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor manual
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Registro de usuario
    public UserResponse register(RegisterAdminDTO request) {
        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        // En Basic Auth no generamos token, solo confirmamos
        UserResponse response = new UserResponse();
        response.setToken("Usuario registrado correctamente: " + user.getEmail());
        return response;
    }

    // ✅ Login (opcional, solo devuelve mensaje)
    public UserResponse login(UserLoginRequest request) {
        // En Basic Auth, Spring valida automáticamente las credenciales.
        // Aquí solo devolvemos un mensaje de éxito.
        UserResponse response = new UserResponse();
        response.setToken("Login exitoso para: " + request.getEmail());
        return response;
    }
}
