package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    UserRepository userRepository;

    public boolean addConnection(String currentUserEmail, String email) {

        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new RuntimeException("Current user not found"));
        User userToConnect = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("The user you want to connect to was not found"));

        if (userRepository.existsByEmail(email)) {
            if (currentUser.getConnections().contains(userToConnect)) {
                return false;
            }
            currentUser.getConnections().add(userToConnect);
            userRepository.save(currentUser);
            return true;
        }
        return false;
    }
}
