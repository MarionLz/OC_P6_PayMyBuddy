package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.UserNotFoundException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the LoginService class.
 * Verifies the behavior of the loadUserByUsername method under various scenarios.
 */
@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    /**
     * Mocked UserRepository for simulating database interactions.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Injected instance of LoginService to be tested.
     */
    @InjectMocks
    private LoginService loginService = new LoginService();

    /**
     * Tests that loadUserByUsername returns user details when the email exists in the database.
     */
    @Test
    void testLoadUserByUsername_ShouldReturnUserDetailsWhenEmailExists() {

        // Arrange: Create a mock user and set up the repository to return it
        User user = new User();
        user.setEmail("existent@example.com");
        user.setPassword("password123");
        when(userRepository.findByEmail("existent@example.com")).thenReturn(java.util.Optional.of(user));

        // Act: Call the method to load the user by username
        loginService.loadUserByUsername("existent@example.com");

        // Assert: Verify that the repository was queried once
        verify(userRepository, times(1)).findByEmail("existent@example.com");
    }

    /**
     * Tests that loadUserByUsername throws a UserNotFoundException when the email does not exist in the database.
     */
    @Test
    void testLoadUserByUsername_ShouldThrowExceptionWhenEmailDoesNotExist() {

        // Arrange: Set up the repository to return an empty result
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(java.util.Optional.empty());

        // Act & Assert: Verify that the exception is thrown and the repository was queried once
        assertThrows(UserNotFoundException.class, () -> loginService.loadUserByUsername("nonexistent@example.com"));
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}