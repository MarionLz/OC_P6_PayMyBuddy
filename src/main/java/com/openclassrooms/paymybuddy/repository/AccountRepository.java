package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Account entities.
 * Extends the CrudRepository interface to provide CRUD operations.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}