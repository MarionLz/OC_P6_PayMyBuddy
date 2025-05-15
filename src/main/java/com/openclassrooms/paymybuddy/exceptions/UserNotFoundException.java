package com.openclassrooms.paymybuddy.exceptions;

/**
 * Exception thrown when a user is not found in the system.
 * This exception is used to indicate that an operation was attempted
 * with a user email that does not exist in the database.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with a detailed message
     * including the email of the user that was not found.
     *
     * @param email the email of the user that could not be found.
     */
    public UserNotFoundException(String email) {
        super("User not found: " + email);
    }
}
