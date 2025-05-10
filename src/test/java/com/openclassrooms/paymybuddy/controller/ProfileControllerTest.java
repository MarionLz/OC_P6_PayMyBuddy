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

@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private Model model;
    @Mock
    private Principal principal;

    @InjectMocks
    private ProfileController profileController;

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

    @Test
    void testShowProfile_ShouldThrowExceptionWhenUserDoesNotExist() {

        when(principal.getName()).thenReturn("nonexistent@example.com");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(java.util.Optional.empty());

        profileController.userRepository = userRepository;

        assertThrows(UsernameNotFoundException.class, () -> profileController.showProfile(model, principal));
    }
}