package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectionServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ConnectionService connectionService;

    @Test
    void testAddConnection_ShouldReturnSuccessWhenUserIsAdded() {

        User currentUser = new User();
        currentUser.setEmail("currentuser@example.com");
        User userToConnect = new User();
        userToConnect.setEmail("newconnection@example.com");

        when(userRepository.findByEmail("currentuser@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail("newconnection@example.com")).thenReturn(Optional.of(userToConnect));

        connectionService.userRepository = userRepository;
        RequestResult result = connectionService.addConnection("currentuser@example.com", "newconnection@example.com");

        assertEquals(true, result.isSuccess());
        assertEquals("Nouvelle relation ajoutée avec succès.", result.getMessage());
        verify(userRepository, times(1)).save(currentUser);
    }

    @Test
    void testAddConnection_ShouldReturnErrorWhenAddingSelf() {

        User currentUser = new User();
        currentUser.setEmail("currentuser@example.com");

        when(userRepository.findByEmail("currentuser@example.com")).thenReturn(Optional.of(currentUser));

        connectionService.userRepository = userRepository;
        RequestResult result = connectionService.addConnection("currentuser@example.com", "currentuser@example.com");

        assertEquals(false, result.isSuccess());
        assertEquals("Vous ne pouvez pas vous ajouter vous-même.", result.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAddConnection_ShouldReturnErrorWhenUserDoesNotExist() {

        User currentUser = new User();
        currentUser.setEmail("currentuser@example.com");

        when(userRepository.findByEmail("currentuser@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        connectionService.userRepository = userRepository;
        RequestResult result = connectionService.addConnection("currentuser@example.com", "nonexistent@example.com");

        assertEquals(false, result.isSuccess());
        assertEquals("L'utilisateur n'existe pas.", result.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAddConnection_ShouldReturnErrorWhenUserAlreadyConnected() {

        User currentUser = new User();
        currentUser.setEmail("currentuser@example.com");
        User userToConnect = new User();
        userToConnect.setEmail("existingconnection@example.com");
        currentUser.getConnections().add(userToConnect);

        when(userRepository.findByEmail("currentuser@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail("existingconnection@example.com")).thenReturn(Optional.of(userToConnect));

        connectionService.userRepository = userRepository;
        RequestResult result = connectionService.addConnection("currentuser@example.com", "existingconnection@example.com");

        assertEquals(false, result.isSuccess());
        assertEquals("L'utilisateur est déjà dans votre liste de relations.", result.getMessage());
        verify(userRepository, never()).save(any());
    }
}
