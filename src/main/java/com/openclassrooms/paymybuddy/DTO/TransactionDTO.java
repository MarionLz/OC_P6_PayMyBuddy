package com.openclassrooms.paymybuddy.DTO;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a transaction.
 * Contains the details of a transaction including the name, description, and amount.
 */
@Data
public class TransactionDTO {

    private String name;
    private String description;
    private int amount;

    /**
     * Constructs a new TransactionDTO with the specified name, description, and amount.
     *
     * @param name the name of the transaction.
     * @param description the description of the transaction.
     * @param amount the amount involved in the transaction.
     */
    public TransactionDTO(String name, String description, int amount) {
        this.name = name;
        this.description = description;
        this.amount = amount;
    }
}