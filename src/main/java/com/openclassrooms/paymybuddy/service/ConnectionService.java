package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.utils.AddConnectionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    UserRepository userRepository;

    public AddConnectionResult addConnection(String currentUserEmail, String email) {

        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new RuntimeException("Current user not found"));

        if (currentUserEmail.equals(email)) {
            return new AddConnectionResult(false, "Vous ne pouvez pas vous ajouter vous-même.");
        }

        Optional<User> optionalUserToConnect = userRepository.findByEmail(email);
        if(optionalUserToConnect.isEmpty()) {
            return new AddConnectionResult(false, "L'utilisateur n'existe pas.");
        }
        User userToConnect = optionalUserToConnect.get();

        if (currentUser.getConnections().contains(userToConnect)) {
            return new AddConnectionResult(false, "L'utilisateur est déjà dans votre liste de relations.");
        }

        currentUser.getConnections().add(userToConnect);
        userRepository.save(currentUser);
        return new AddConnectionResult(true, "Nouvelle relation ajoutée avec succès.");
    }
}
