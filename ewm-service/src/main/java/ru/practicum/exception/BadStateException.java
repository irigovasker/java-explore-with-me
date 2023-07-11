package ru.practicum.exception;

public class BadStateException extends RuntimeException {
    public BadStateException(final String message) {
        super(message);
    }
}

