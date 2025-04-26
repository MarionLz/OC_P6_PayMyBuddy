package com.openclassrooms.paymybuddy.model;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @OneToMany
    @JoinTable(
            name = "connections",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "connectionId")
    )
    private List<User> connections = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Transaction> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receivedTransactions = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account account;
}