package com.buzzword;

/**
 * Custom runtime exception to be thrown whenever a 
 * user cannot be successfully authenticated.
 * 
 * @author Ben Edens
 * @version 1.0
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
