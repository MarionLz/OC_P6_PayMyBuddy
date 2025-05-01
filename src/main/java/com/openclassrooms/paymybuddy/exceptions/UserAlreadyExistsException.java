package com.openclassrooms.paymybuddy.exceptions;

/**
 * Exception thrown when an email already exists in the system.
 * This exception is used to indicate that the user is trying to register with an email
 * that is already associated with an existing account.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}