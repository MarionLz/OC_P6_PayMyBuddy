package com.openclassrooms.paymybuddy.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Username is required and cannot be blank")
    @Size(min = 2, max = 50, message = "Username should be between 2 and 50 characters")
    private String username;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required and cannot be blank")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
