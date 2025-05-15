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

/**
 * Unit tests for the RegisterService class.
 * Verifies the behavior of the register method under various scenarios.
 */
@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest {

    /**
     * Mocked PasswordEncoder for simulating password encryption.
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Mocked AccountRepository for simulating account-related database interactions.
     */
    @Mock
    private AccountRepository accountRepository;

    /**
     * Mocked UserRepository for simulating user-related database interactions.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Injected instance of RegisterService to be tested.
     */
    @InjectMocks
    RegisterService registerService;

    /**
     * Tests that the register method creates a new user and account
     * when the email does not already exist in the database.
     */
    @Test
    void testRegister_ShouldCreateUserAndAccountWhenEmailDoesNotExist() {

        // Arrange: Create a registration request and mock repository behavior
        RegisterRequestDTO registerRequest = new RegisterRequestDTO("testuser", "test@example.com", "password123");
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        // Act: Call the register method
        registerService.register(registerRequest);

        // Assert: Verify that the user and account are saved
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Tests that the register method throws a UserAlreadyExistsException
     * when the email already exists in the database.
     */
    @Test
    void testRegister_ShouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange: Create a registration request and mock repository behavior
        RegisterRequestDTO registerRequest = new RegisterRequestDTO("testuser", "test@example.com", "password123");
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // Act & Assert: Verify that the exception is thrown and no entities are saved
        assertThrows(UserAlreadyExistsException.class, () -> registerService.register(registerRequest));
        verify(accountRepository, never()).save(any(Account.class));
        verify(userRepository, never()).save(any(User.class));
    }
}