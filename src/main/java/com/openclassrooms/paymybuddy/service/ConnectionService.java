package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import com.openclassrooms.paymybuddy.utils.UserUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing user connections.
 * Provides functionality to add a connection between users.
 */
@Service
public class ConnectionService {

    private static final Logger logger = LogManager.getLogger("ConnectionService");

    @Autowired
    UserRepository userRepository;

    /**
     * Adds a connection between the current user and a target user.
     *
     * @param currentUserEmail the email of the current user
     * @param targetEmail the email of the target user to connect with
     * @return a RequestResult indicating the success or failure of the operation
     */
    public RequestResult addConnection(String currentUserEmail, String targetEmail) {

        logger.info("Attempting to add connection from '{}' to '{}'", currentUserEmail, targetEmail);

        // Find the current user by email or throw an exception if not found
        User currentUser = UserUtils.findUserByEmailOrThrow(userRepository, currentUserEmail);

        // Prevent the user from connecting with themselves
        if (currentUserEmail.equals(targetEmail)) {
            logger.warn("User '{}' attempted to connect with themselves.", currentUserEmail);
            return new RequestResult(false, "Vous ne pouvez pas vous ajouter vous-même.");
        }

        // Check if the target user exists
        Optional<User> optionalUserToConnect = userRepository.findByEmail(targetEmail);
        if (optionalUserToConnect.isEmpty()) {
            logger.warn("Target user '{}' does not exist. Connection aborted.", targetEmail);
            return new RequestResult(false, "L'utilisateur n'existe pas.");
        }
        User userToConnect = optionalUserToConnect.get();

        // Check if the connection already exists
        if (currentUser.getConnections().contains(userToConnect)) {
            logger.warn("User '{}' is already connected with '{}'.", currentUserEmail, targetEmail);
            return new RequestResult(false, "L'utilisateur est déjà dans votre liste de relations.");
        }

        // Add the connection and save the updated user
        currentUser.getConnections().add(userToConnect);
        userRepository.save(currentUser);
        logger.info("Connection successfully added: '{}' → '{}'", currentUserEmail, targetEmail);
        return new RequestResult(true, "Nouvelle relation ajoutée avec succès.");
    }
}
