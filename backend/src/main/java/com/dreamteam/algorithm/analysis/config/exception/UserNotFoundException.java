package com.dreamteam.algorithm.analysis.config.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(String.format("User not found with id: %s", message));
    }
}
