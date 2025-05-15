package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConnectionService {

    private static final Logger logger = LogManager.getLogger("ConnectionService");

    @Autowired
    UserRepository userRepository;

    public RequestResult addConnection(String currentUserEmail, String targetEmail) {

        logger.info("Attempting to add connection from '{}' to '{}'", currentUserEmail, targetEmail);

        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> {
            logger.error("Current user '{}' not found in database.", currentUserEmail);
            return new RuntimeException("Current user not found");
        });

        if (currentUserEmail.equals(targetEmail)) {
            logger.warn("User '{}' attempted to connect with themselves.", currentUserEmail);
            return new RequestResult(false, "Vous ne pouvez pas vous ajouter vous-même.");
        }

        Optional<User> optionalUserToConnect = userRepository.findByEmail(targetEmail);
        if (optionalUserToConnect.isEmpty()) {
            logger.warn("Target user '{}' does not exist. Connection aborted.", targetEmail);
            return new RequestResult(false, "L'utilisateur n'existe pas.");
        }
        User userToConnect = optionalUserToConnect.get();

        if (currentUser.getConnections().contains(userToConnect)) {
            logger.warn("User '{}' is already connected with '{}'.", currentUserEmail, targetEmail);
            return new RequestResult(false, "L'utilisateur est déjà dans votre liste de relations.");
        }

        currentUser.getConnections().add(userToConnect);
        userRepository.save(currentUser);
        logger.info("Connection successfully added: '{}' → '{}'", currentUserEmail, targetEmail);
        return new RequestResult(true, "Nouvelle relation ajoutée avec succès.");
    }
}
