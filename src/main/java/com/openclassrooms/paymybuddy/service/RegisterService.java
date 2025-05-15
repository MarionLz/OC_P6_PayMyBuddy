package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import com.openclassrooms.paymybuddy.model.Account;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.repository.AccountRepository;
import com.openclassrooms.paymybuddy.exceptions.UserAlreadyExistsException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private static final Logger logger = LogManager.getLogger("RegisterService");

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    public void register(RegisterRequestDTO registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logger.error("User already exists with email: {}", registerRequest.getEmail());
            throw new UserAlreadyExistsException("An account with this email already exists : " + registerRequest.getEmail());
        }

        Account account = new Account();
        account.setBalance(1000);

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAccount(account);

        accountRepository.save(account);
        userRepository.save(user);
        logger.info("User and account saved");
    }
}
