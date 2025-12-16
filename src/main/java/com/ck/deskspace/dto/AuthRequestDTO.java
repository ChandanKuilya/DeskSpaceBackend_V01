package com.ck.deskspace.dto;

import com.ck.deskspace.models.enums.Role;
import lombok.Data;

@Data
public class AuthRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role; // ADMIN or MEMBER
}
