package com.bridgelabz.quantitymeasurement;

/**
 * Custom unchecked exception for database related failures in UC16.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

