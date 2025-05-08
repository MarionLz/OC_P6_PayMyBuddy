package com.openclassrooms.paymybuddy.utils;

public class RequestResult {

    private final boolean success;
    private final String message;

    public RequestResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
