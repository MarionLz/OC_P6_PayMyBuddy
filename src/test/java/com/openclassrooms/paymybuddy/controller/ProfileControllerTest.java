package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProfileController class.
 * Verifies the behavior of the profile-related endpoints and interactions with the repository and model.
 */
@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {

    /**
     * Mocked UserRepository for simulating database interactions.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mocked Model for simulating the view model.
     */
    @Mock
    private Model model;

    /**
     * Mocked Principal for simulating the currently authenticated user.
     */
    @Mock
    private Principal principal;

    /**
     * Injected instance of ProfileController to be tested.
     */
    @InjectMocks
    private ProfileController profileController;

    /**
     * Tests that the showProfile method adds the user to the model when the user exists.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testShowProfile_ShouldAddUserToModelWhenUserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));

        profileController.userRepository = userRepository;
        profileController.showProfile(model, principal);

        verify(model, times(1)).addAttribute("user", user);
    }

    /**
     * Tests that the showProfile method throws a UsernameNotFoundException when the user does not exist.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testShowProfile_ShouldThrowExceptionWhenUserDoesNotExist() {
        when(principal.getName()).thenReturn("nonexistent@example.com");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(java.util.Optional.empty());

        profileController.userRepository = userRepository;

        assertThrows(UsernameNotFoundException.class, () -> profileController.showProfile(model, principal));
    }
}