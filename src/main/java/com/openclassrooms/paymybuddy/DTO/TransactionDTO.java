package com.openclassrooms.paymybuddy.DTO;

import lombok.Data;

@Data
public class TransactionDTO {

    private String name;
    private String description;
    private int amount;

    public TransactionDTO(String name, String description, int amount) {
        this.name = name;
        this.description = description;
        this.amount = amount;
    }
}
