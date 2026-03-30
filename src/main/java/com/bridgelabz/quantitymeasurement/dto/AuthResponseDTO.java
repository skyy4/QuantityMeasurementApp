package com.bridgelabz.quantitymeasurement.dto;

public class AuthResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private String email;
    private String name;
    private String role;
    private String imageUrl;

    public AuthResponseDTO(String token, String email, String name, String role, String imageUrl) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.role = role;
        this.imageUrl = imageUrl;
    }

    public String getToken() { return token; }
    public String getTokenType() { return tokenType; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getImageUrl() { return imageUrl; }
}
