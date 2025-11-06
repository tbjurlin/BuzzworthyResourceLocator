package com.buzzword;

public class RecordDoesNotExistException extends RuntimeException {
    public RecordDoesNotExistException() {
        super();
    }

    public RecordDoesNotExistException(String message) {
        super(message);
    }

    public RecordDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}