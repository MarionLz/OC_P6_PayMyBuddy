package com.openclassrooms.paymybuddy.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransferRequestDTO {

    @NotBlank(message = "Veuillez entrer une description.")
    private String description;

    @NotBlank(message = "Veuillez entrer l'email du destinataire.")
    private String receiverEmail;

    @NotNull(message = "Veuillez entrer un montant.")
    @Min(value = 1, message = "Le montant doit être supérieur à zéro.")
    private Double amount;
}
