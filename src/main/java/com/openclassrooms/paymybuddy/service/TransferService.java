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
import com.openclassrooms.paymybuddy.utils.UserUtils;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling user transfers and related operations.
 * Provides functionality to manage connections, balances, transactions, and transfers.
 */
@Service
public class TransferService {

    private static final Logger logger = LogManager.getLogger("TransferService");

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    /**
     * Retrieves the list of connections for a user identified by their email.
     *
     * @param email the email of the user whose connections are to be fetched
     * @return a list of ConnectionDTO objects representing the user's connections
     */
    public List<ConnectionDTO> getConnections(String email) {

        logger.info("Fetching connections for user '{}'", email);
        User user = UserUtils.findUserByEmailOrThrow(userRepository, email);

        return user.getConnections().stream()
                .map(connection -> new ConnectionDTO(connection.getEmail()))
                .toList();
    }

    /**
     * Retrieves the balance of a user identified by their email.
     *
     * @param email the email of the user whose balance is to be fetched
     * @return the balance of the user's account
     */
    public int getUserBalance(String email) {

        logger.info("Fetching balance for user '{}'", email);
        User user = UserUtils.findUserByEmailOrThrow(userRepository, email);
        return user.getAccount().getBalance();
    }

    /**
     * Retrieves the list of transactions sent by a user identified by their email.
     *
     * @param email the email of the user whose transactions are to be fetched
     * @return a list of TransactionDTO objects representing the user's sent transactions
     */
    public List<TransactionDTO> getTransactions(String email) {

        logger.info("Fetching sent transactions for user '{}'", email);
        User user = UserUtils.findUserByEmailOrThrow(userRepository, email);
        return user.getSentTransactions().stream()
                .map(transaction -> new TransactionDTO(transaction.getReceiver().getUsername(), transaction.getDescription(), transaction.getAmount()))
                .toList();
    }

    /**
     * Processes a transfer request from the current user to a receiver.
     * Updates the balances of both users and records the transaction.
     *
     * @param transferRequest the details of the transfer request
     * @param currentUserEmail the email of the user initiating the transfer
     * @return a RequestResult indicating the success or failure of the transfer
     */
    @Transactional
    public RequestResult transfer(TransferRequestDTO transferRequest, String currentUserEmail) {

        logger.info("User '{}' initiates a transfer to '{}', amount: {}",
                currentUserEmail, transferRequest.getReceiverEmail(), transferRequest.getAmount());

        User currentUser = UserUtils.findUserByEmailOrThrow(userRepository, currentUserEmail);
        User receiver = UserUtils.findUserByEmailOrThrow(userRepository, transferRequest.getReceiverEmail());

        Account currentUserAccount = currentUser.getAccount();
        int newBalance = currentUserAccount.getBalance() - transferRequest.getAmount();

        if (newBalance < 0) {
            logger.warn("Transfer aborted: user '{}' has insufficient funds (balance: {}, attempted transfer: {})",
                    currentUserEmail, currentUserAccount.getBalance(), transferRequest.getAmount());
            return new RequestResult(false, "Solde insuffisant. Veuillez réapprovisionner votre compte.");
        }

        currentUserAccount.setBalance(newBalance);
        accountRepository.save(currentUserAccount);
        logger.debug("Debited {} from user '{}' account. New balance: {}", transferRequest.getAmount(), currentUserEmail, newBalance);

        Account receiverAccount = receiver.getAccount();
        receiverAccount.setBalance(receiverAccount.getBalance() + transferRequest.getAmount());
        accountRepository.save(receiverAccount);
        logger.debug("Credited {} to receiver '{}' account.", transferRequest.getAmount(), transferRequest.getReceiverEmail());

        Transaction transaction = new Transaction();
        transaction.setAmount(transferRequest.getAmount());
        transaction.setDescription(transferRequest.getDescription());
        transaction.setSender(currentUser);
        transaction.setReceiver(receiver);
        transactionRepository.save(transaction);
        logger.info("Transfer transaction saved: {} → {}, amount: {}", currentUserEmail, transferRequest.getReceiverEmail(), transferRequest.getAmount());
        return new RequestResult(true, "Transfert effectué avec succès.");
    }
}