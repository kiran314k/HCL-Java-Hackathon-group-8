package com.hcl.wallet.exception;


/**
 * Exception thrown when the input string for add endpoint
 * is invalid (e.g., too short).
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}