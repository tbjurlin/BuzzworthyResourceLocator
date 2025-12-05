package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ConfigurationExceptionTest {

    @Test
    void testDefaultConstructor() {
        ConfigurationException exception = new ConfigurationException();
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Configuration error occurred";
        ConfigurationException exception = new ConfigurationException(message);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Configuration error occurred";
        Throwable cause = new RuntimeException("Root cause");
        ConfigurationException exception = new ConfigurationException(message, cause);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionIsRuntimeException() {
        ConfigurationException exception = new ConfigurationException();
        assertNotNull(exception instanceof RuntimeException);
    }
}
