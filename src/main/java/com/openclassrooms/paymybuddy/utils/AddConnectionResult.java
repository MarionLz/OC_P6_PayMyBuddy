package com.openclassrooms.paymybuddy.utils;

public class AddConnectionResult {

    private final boolean success;
    private final String message;

    public AddConnectionResult(boolean success, String message) {
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
