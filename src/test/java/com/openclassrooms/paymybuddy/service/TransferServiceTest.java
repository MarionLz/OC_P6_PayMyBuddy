package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.DTO.ConnectionDTO;
import com.openclassrooms.paymybuddy.DTO.TransactionDTO;
import com.openclassrooms.paymybuddy.DTO.TransferRequestDTO;
import com.openclassrooms.paymybuddy.model.Account;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.AccountRepository;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void testGetConnections_ShouldReturnConnectionsForValidUser() {

        User user = new User();
        user.setEmail("user@example.com");
        User connection = new User();
        connection.setEmail("connection@example.com");
        user.setConnections(List.of(connection));

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        List<ConnectionDTO> connections = transferService.getConnections("user@example.com");

        assertEquals(1, connections.size());
        assertEquals("connection@example.com", connections.get(0).getEmail());
    }

    @Test
    void testGetConnections_ShouldThrowExceptionForNonExistentUser() {

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transferService.getConnections("nonexistent@example.com"));
    }

    @Test
    void testGetUserBalance_ShouldReturnCorrectBalance() {

        User user = new User();
        Account account = new Account();
        account.setBalance(500);
        user.setAccount(account);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        int balance = transferService.getUserBalance("user@example.com");

        assertEquals(500, balance);
    }

    @Test
    void testGetUserBalance_ShouldThrowExceptionForNonExistentUser() {

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transferService.getUserBalance("nonexistent@example.com"));
    }

    @Test
    void testGetTransactions_ShouldReturnTransactionsForValidUser() {

        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        Transaction transaction = new Transaction();
        transaction.setDescription("Payment");
        transaction.setAmount(100);
        User receiver1 = new User();
        receiver1.setUsername("receiver1");
        transaction.setReceiver(receiver1);

        user.setSentTransactions(Arrays.asList(transaction));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        List<TransactionDTO> result = transferService.getTransactions(email);

        assertEquals(1, result.size());
        assertEquals("receiver1", result.get(0).getName());
        assertEquals("Payment", result.get(0).getDescription());
        assertEquals(100, result.get(0).getAmount());
    }

    @Test
    void testGetTransactions_ShouldThrowExceptionForNonExistentUser() {

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transferService.getUserBalance("nonexistent@example.com"));
    }

    @Test
    void testTransfer_ShouldSucceedWhenSufficientBalance() {

        User sender = new User();
        Account senderAccount = new Account();
        senderAccount.setBalance(200);
        sender.setAccount(senderAccount);

        User receiver = new User();
        Account receiverAccount = new Account();
        receiver.setAccount(receiverAccount);

        TransferRequestDTO transferRequest = new TransferRequestDTO();
        transferRequest.setReceiverEmail("receiver@example.com");
        transferRequest.setAmount(100);

        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));

        RequestResult result = transferService.transfer(transferRequest, "sender@example.com");

        assertTrue(result.isSuccess());
        assertEquals("Transfert effectué avec succès.", result.getMessage());
        assertEquals(100, senderAccount.getBalance());
        assertEquals(100, receiverAccount.getBalance());
    }

    @Test
    void testTransfer_ShouldFailWhenInsufficientBalance() {

        User sender = new User();
        Account senderAccount = new Account();
        senderAccount.setBalance(50);
        sender.setAccount(senderAccount);

        User receiver = new User();
        Account receiverAccount = new Account();
        receiver.setAccount(receiverAccount);

        TransferRequestDTO transferRequest = new TransferRequestDTO();
        transferRequest.setReceiverEmail("receiver@example.com");
        transferRequest.setAmount(100);

        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));

        RequestResult result = transferService.transfer(transferRequest, "sender@example.com");

        assertFalse(result.isSuccess());
        assertEquals("Solde insuffisant. Veuillez réapprovisionner votre compte.", result.getMessage());
    }
}
