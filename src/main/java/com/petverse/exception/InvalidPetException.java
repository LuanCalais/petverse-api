package com.petverse.exception;

public class InvalidPetException extends RuntimeException {
    public InvalidPetException(String message) {
        super(message);
    }

    public InvalidPetException(String message, Throwable cause) {
        super(message, cause);
    }
}
