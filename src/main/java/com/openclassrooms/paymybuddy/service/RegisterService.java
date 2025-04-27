package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import com.openclassrooms.paymybuddy.model.Account;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.repository.AccountRepository;
import com.openclassrooms.paymybuddy.exceptions.EmailAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    public void register(RegisterRequestDTO registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("An account with this email already exists.");
        }

        Account account = new Account();
        account.setBalance(1000.0);

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAccount(account);

        accountRepository.save(account);
        userRepository.save(user);
    }
}
