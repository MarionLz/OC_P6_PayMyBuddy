package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Extends the CrudRepository interface to provide CRUD operations.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check for existence
     * @return true if a user with the given email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to find
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
}