package com.example.vet.service;

import com.example.vet.repository.UserRepository;
import com.example.vet.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUsernameById(Integer userId) {
        User user = userRepository.findById(userId) 
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            
        return user.getNombre(); 
    }
    
}
