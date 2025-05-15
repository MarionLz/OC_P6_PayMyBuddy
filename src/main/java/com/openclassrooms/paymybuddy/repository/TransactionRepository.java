package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Transaction entities.
 * Extends the CrudRepository interface to provide CRUD operations.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}