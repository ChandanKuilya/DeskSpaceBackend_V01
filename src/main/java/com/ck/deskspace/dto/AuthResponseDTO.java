package com.ck.deskspace.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    // Constructor for just message/token (backward compatibility or simple messages)
    public AuthResponseDTO(String token) {
        this.token = token;
    }

    // Full constructor
    public AuthResponseDTO(String token, String firstName, String lastName, String email, String role) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}
