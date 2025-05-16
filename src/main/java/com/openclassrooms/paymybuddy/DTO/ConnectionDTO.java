package com.openclassrooms.paymybuddy.DTO;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a connection.
 * Contains the email of a connected user.
 */
@Data
public class ConnectionDTO {

    private String email;

    /**
     * Constructs a new ConnectionDTO with the specified email.
     *
     * @param email the email of the connected user.
     */
    public ConnectionDTO(String email) {
        this.email = email;
    }
}
