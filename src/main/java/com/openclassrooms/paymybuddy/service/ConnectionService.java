package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    UserRepository userRepository;

    public RequestResult addConnection(String currentUserEmail, String email) {

        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new RuntimeException("Current user not found"));

        if (currentUserEmail.equals(email)) {
            return new RequestResult(false, "Vous ne pouvez pas vous ajouter vous-même.");
        }

        Optional<User> optionalUserToConnect = userRepository.findByEmail(email);
        if(optionalUserToConnect.isEmpty()) {
            return new RequestResult(false, "L'utilisateur n'existe pas.");
        }
        User userToConnect = optionalUserToConnect.get();

        if (currentUser.getConnections().contains(userToConnect)) {
            return new RequestResult(false, "L'utilisateur est déjà dans votre liste de relations.");
        }

        currentUser.getConnections().add(userToConnect);
        userRepository.save(currentUser);
        return new RequestResult(true, "Nouvelle relation ajoutée avec succès.");
    }
}
