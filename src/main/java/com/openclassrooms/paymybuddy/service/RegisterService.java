package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import com.openclassrooms.paymybuddy.model.Account;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.repository.AccountRepository;
import com.openclassrooms.paymybuddy.exceptions.UserAlreadyExistsException;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user registration.
 * Provides functionality to register a new user and create an associated account.
 */
@Service
public class RegisterService {

    private static final Logger logger = LogManager.getLogger("RegisterService");

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Registers a new user with the provided registration details.
     * Creates a new account with a default balance and associates it with the user.
     *
     * @param registerRequest the registration details provided by the user
     * @throws UserAlreadyExistsException if a user with the given email already exists
     */
    public void register(RegisterRequestDTO registerRequest) {

        // Check if a user with the given email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logger.error("User already exists with email: {}", registerRequest.getEmail());
            throw new UserAlreadyExistsException("An account with this email already exists : " + registerRequest.getEmail());
        }

        // Create a new account with a default balance
        Account account = new Account();
        account.setBalance(1000);

        // Create a new user and set their details
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAccount(account);

        // Save the account and user to the database
        accountRepository.save(account);
        userRepository.save(user);
        logger.info("User and account saved");
    }
}
