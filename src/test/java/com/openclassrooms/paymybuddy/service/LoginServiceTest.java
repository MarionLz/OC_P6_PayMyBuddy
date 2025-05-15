package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.UserNotFoundException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService = new LoginService();

    @Test
    void testLoadUserByUsername_ShouldReturnUserDetailsWhenEmailExists() {

        User user = new User();
        user.setEmail("existent@example.com");
        user.setPassword("password123");
        when(userRepository.findByEmail("existent@example.com")).thenReturn(java.util.Optional.of(user));

        loginService.loadUserByUsername("existent@example.com");

        verify(userRepository, times(1)).findByEmail("existent@example.com");
    }

    @Test
    void testLoadUserByUsername_ShouldThrowExceptionWhenEmailDoesNotExist() {

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> loginService.loadUserByUsername("nonexistent@example.com"));

        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}
