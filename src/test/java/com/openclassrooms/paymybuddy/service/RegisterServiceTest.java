package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import com.openclassrooms.paymybuddy.exceptions.UserAlreadyExistsException;
import com.openclassrooms.paymybuddy.model.Account;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.AccountRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    RegisterService registerService;

    @Test
    void testRegister_ShouldCreateUserAndAccountWhenEmailDoesNotExist() {

        RegisterRequestDTO registerRequest = new RegisterRequestDTO("testuser", "test@example.com", "password123");
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        registerService.register(registerRequest);

        verify(accountRepository, times(1)).save(any(Account.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_ShouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterRequestDTO registerRequest = new RegisterRequestDTO("testuser", "test@example.com", "password123");
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> registerService.register(registerRequest));

        verify(accountRepository, never()).save(any(Account.class));
        verify(userRepository, never()).save(any(User.class));
    }
}
