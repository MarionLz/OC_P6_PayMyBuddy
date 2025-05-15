package com.openclassrooms.paymybuddy.utils;

/**
 * Utility class representing the result of a request or operation.
 * Encapsulates the success status and an associated message.
 */
public class RequestResult {

    private final boolean success;
    private final String message;

    /**
     * Constructs a new RequestResult with the specified success status and message.
     *
     * @param success true if the request or operation was successful, false otherwise
     * @param message a message describing the result of the request or operation
     */
    public RequestResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Returns the success status of the request or operation.
     *
     * @return true if the request or operation was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the message associated with the request or operation result.
     *
     * @return a string containing the result message
     */
    public String getMessage() {
        return message;
    }
}
