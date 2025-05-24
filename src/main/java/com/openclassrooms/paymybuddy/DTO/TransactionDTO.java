package com.openclassrooms.paymybuddy.DTO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a transaction.
 * Contains the details of a transaction including the name, description, and amount.
 */
@Data
public class TransactionDTO {

    private String name;
    private String description;
    private int amount;
    private LocalDateTime timestamp;
    private String direction; // "SENT" or "RECEIVED"

    /**
     * Constructs a new TransactionDTO with the specified name, description, amount and timestamp.
     *
     * @param name the name of the transaction.
     * @param description the description of the transaction.
     * @param amount the amount involved in the transaction.
     * @param timestamp the timestamp of the transaction.
     * @param direction the direction of the transaction, either "SENT" or "RECEIVED".
     */
    public TransactionDTO(String name, String description, int amount, LocalDateTime timestamp, String direction) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.timestamp = timestamp;
        this.direction = direction;
    }
}