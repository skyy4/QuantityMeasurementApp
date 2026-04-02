package com.bridgelabz.quantitymeasurement.dto;

public class AuthResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private String email;
    private String name;
    private String role;

    public AuthResponseDTO(String token, String email, String name, String role) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public String getToken() { return token; }
    public String getTokenType() { return tokenType; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }
}
