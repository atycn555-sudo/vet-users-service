package com.example.vet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "The email cannot be empty")
    @Email(message = "The email format is invalid")
    private String email;
    
    @NotBlank(message = "The password cannot be empty.")
    @Size(min = 8, message = "The password must be at least 8 characters long")
    private String password;
    
    private String role;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
