package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.DTO.TransferRequestDTO;
import com.openclassrooms.paymybuddy.model.Account;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.AccountRepository;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    public RequestResult transfer(TransferRequestDTO transferRequest, String currentUserEmail) {

        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new RuntimeException("Current user not found"));
        User receiver = userRepository.findByEmail(transferRequest.getReceiverEmail()).orElseThrow(() -> new RuntimeException("Receiver not found"));

        Account currentUserAccount = currentUser.getAccount();
        currentUserAccount.setBalance(currentUserAccount.getBalance() - transferRequest.getAmount());
        if (currentUserAccount.getBalance() < 0) {
            return new RequestResult(false, "Solde insuffisant. Veuillez réapprovisionner votre compte."); // Insufficient funds
        }
        accountRepository.save(currentUserAccount);

        Account receiverAccount = receiver.getAccount();
        receiverAccount.setBalance(receiverAccount.getBalance() + transferRequest.getAmount());
        accountRepository.save(receiverAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(transferRequest.getAmount());
        transaction.setDescription(transferRequest.getDescription());
        transaction.setSender(currentUser);
        transaction.setReceiver(receiver);
        transactionRepository.save(transaction);
        return new RequestResult(true, "Transfert effectué avec succès.");
    }
}
