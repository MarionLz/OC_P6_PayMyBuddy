package com.openclassrooms.paymybuddy.DTO;

import lombok.Data;

@Data
public class RegisterRequestDTO {

    private String username;
    private String email;
    private String password;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
