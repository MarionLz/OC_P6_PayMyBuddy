package com.openclassrooms.paymybuddy.exceptions;

/**
 * Exception thrown when an email already exists in the system.
 * This exception is used to indicate that the user is trying to register with an email
 * that is already associated with an existing account.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}