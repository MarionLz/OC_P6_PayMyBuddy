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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the TransferService class.
 * Verifies the behavior of transfer-related methods under various scenarios.
 */
@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    /**
     * Mocked UserRepository for simulating user-related database interactions.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mocked TransactionRepository for simulating transaction-related database interactions.
     */
    @Mock
    private TransactionRepository transactionRepository;

    /**
     * Mocked AccountRepository for simulating account-related database interactions.
     */
    @Mock
    private AccountRepository accountRepository;

    /**
     * Injected instance of TransferService to be tested.
     */
    @InjectMocks
    private TransferService transferService;

    /**
     * Tests that getConnections returns a list of connections for a valid user.
     */
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

    /**
     * Tests that getConnections throws an exception for a non-existent user.
     */
    @Test
    void testGetConnections_ShouldThrowExceptionForNonExistentUser() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transferService.getConnections("nonexistent@example.com"));
    }

    /**
     * Tests that getUserBalance returns the correct balance for a valid user.
     */
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

    /**
     * Tests that getUserBalance throws an exception for a non-existent user.
     */
    @Test
    void testGetUserBalance_ShouldThrowExceptionForNonExistentUser() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transferService.getUserBalance("nonexistent@example.com"));
    }

    /**
     * Tests that getTransactions returns a list of transactions for a valid user.
     */
    @Test
    void testGetTransactions_ShouldReturnTransactionsForValidUser() {
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        Transaction sentTransaction  = new Transaction();
        sentTransaction .setDescription("Payment sent");
        sentTransaction .setAmount(100);
        User receiver = new User();
        receiver.setUsername("receiver1");
        sentTransaction .setReceiver(receiver);
        sentTransaction.setTimestamp(LocalDateTime.now().minusDays(1));

        Transaction receivedTransaction = new Transaction();
        receivedTransaction.setDescription("Payment received");
        receivedTransaction.setAmount(50);
        User sender = new User();
        sender.setUsername("sender1");
        receivedTransaction.setSender(sender);
        receivedTransaction.setTimestamp(LocalDateTime.now());

        user.setSentTransactions(Arrays.asList(sentTransaction ));
        user.setReceivedTransactions(List.of(receivedTransaction));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        List<TransactionDTO> result = transferService.getTransactions(email);

        assertEquals(2, result.size());
        assertEquals("sender1", result.get(0).getName());
        assertEquals("Payment received", result.get(0).getDescription());
        assertEquals(50, result.get(0).getAmount());
        assertEquals("receiver1", result.get(1).getName());
        assertEquals("Payment sent", result.get(1).getDescription());
        assertEquals(100, result.get(1).getAmount());
    }

    /**
     * Tests that getTransactions throws an exception for a non-existent user.
     */
    @Test
    void testGetTransactions_ShouldThrowExceptionForNonExistentUser() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transferService.getUserBalance("nonexistent@example.com"));
    }

    /**
     * Tests that transfer succeeds when the sender has sufficient balance.
     */
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

    /**
     * Tests that transfer fails when the sender has insufficient balance.
     */
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