package com.bridgelabz.quantitymeasurement.dto;

public class AuthResponseDTO {
    private String token;
    private String name;
    private String email;
    private String role;

    public AuthResponseDTO(String token, String name, String email, String role) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Explicit Getters and Setters for Jackson (JSON Converter)
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
