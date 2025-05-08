package com.openclassrooms.paymybuddy.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequestDTO {

    @Size(min = 2, max = 50, message = "Le nom d'utilisateur doit être compris entre 2 et 50 caractères.")
    private String username;

    @NotBlank(message = "Un email est requis.")
    @Email(message = "Entrez un email valide.")
    private String email;

    @Size(min = 8, message = "Veuillez entrer un mot de passe de 8 caractères minimum.")
    private String password;

    public RegisterRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}