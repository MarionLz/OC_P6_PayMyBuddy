package com.openclassrooms.paymybuddy.DTO;

import lombok.Data;

@Data
public class ConnectionDTO {

    private String email;

    public ConnectionDTO(String email) {
        this.email = email;
    }
}
