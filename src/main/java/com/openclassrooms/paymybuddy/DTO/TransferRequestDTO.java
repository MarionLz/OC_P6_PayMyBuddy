package com.openclassrooms.paymybuddy.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for transfer requests.
 * Contains the necessary information to process a transfer operation.
 */
@Data
@NoArgsConstructor
public class TransferRequestDTO {

    @NotBlank(message = "Veuillez entrer une description.")
    private String description;

    @NotBlank(message = "Veuillez entrer l'email du destinataire.")
    private String receiverEmail;

    @NotNull(message = "Veuillez entrer un montant.")
    @Min(value = 1, message = "Le montant doit être supérieur à zéro.")
    private int amount;

    /**
     * Constructs a new TransferRequestDTO with the specified description, recipient email, and amount.
     *
     * @param description the description of the transfer.
     * @param receiverEmail the email of the transfer recipient.
     * @param amount the amount to be transferred.
     */
    public TransferRequestDTO(String description, String receiverEmail, int amount) {
        this.description = description;
        this.receiverEmail = receiverEmail;
        this.amount = amount;
    }
}
