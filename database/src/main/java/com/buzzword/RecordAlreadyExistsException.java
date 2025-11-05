package com.buzzword;

public class RecordAlreadyExistsException extends RuntimeException {
    public RecordAlreadyExistsException() {
        super();
    }

    public RecordAlreadyExistsException(String message) {
        super(message);
    }

    public RecordAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}