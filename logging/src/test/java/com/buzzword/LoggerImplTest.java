package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoggerImplTest {

    private Logger logger;

    @BeforeEach
    public void setUp() {
        logger = new LoggerImpl("TestLogger");
    }

    @Test
    public void testLoggerConstruction() {
        assertNotNull(logger, "Logger should be constructed successfully");
    }

    @Test
    public void testTraceLogging() {
        assertDoesNotThrow(() -> logger.trace("Trace message"), "Trace logging should not throw exception");
    }

    @Test
    public void testDebugLogging() {
        assertDoesNotThrow(() -> logger.debug("Debug message"), "Debug logging should not throw exception");
    }

    @Test
    public void testInfoLogging() {
        assertDoesNotThrow(() -> logger.info("Info message"), "Info logging should not throw exception");
    }

    @Test
    public void testWarnLogging() {
        assertDoesNotThrow(() -> logger.warn("Warning message"), "Warn logging should not throw exception");
    }

    @Test
    public void testErrorLogging() {
        assertDoesNotThrow(() -> logger.error("Error message"), "Error logging should not throw exception");
    }

    @Test
    public void testTraceWithNull() {
        assertDoesNotThrow(() -> logger.trace(null), "Trace logging with null should not throw exception");
    }

    @Test
    public void testDebugWithNull() {
        assertDoesNotThrow(() -> logger.debug(null), "Debug logging with null should not throw exception");
    }

    @Test
    public void testInfoWithNull() {
        assertDoesNotThrow(() -> logger.info(null), "Info logging with null should not throw exception");
    }

    @Test
    public void testWarnWithNull() {
        assertDoesNotThrow(() -> logger.warn(null), "Warn logging with null should not throw exception");
    }

    @Test
    public void testErrorWithNull() {
        assertDoesNotThrow(() -> logger.error(null), "Error logging with null should not throw exception");
    }

    @Test
    public void testTraceWithEmptyString() {
        assertDoesNotThrow(() -> logger.trace(""), "Trace logging with empty string should not throw exception");
    }

    @Test
    public void testDebugWithEmptyString() {
        assertDoesNotThrow(() -> logger.debug(""), "Debug logging with empty string should not throw exception");
    }

    @Test
    public void testInfoWithEmptyString() {
        assertDoesNotThrow(() -> logger.info(""), "Info logging with empty string should not throw exception");
    }

    @Test
    public void testWarnWithEmptyString() {
        assertDoesNotThrow(() -> logger.warn(""), "Warn logging with empty string should not throw exception");
    }

    @Test
    public void testErrorWithEmptyString() {
        assertDoesNotThrow(() -> logger.error(""), "Error logging with empty string should not throw exception");
    }

    @Test
    public void testMultipleLogMessages() {
        assertDoesNotThrow(() -> {
            logger.trace("First trace");
            logger.debug("First debug");
            logger.info("First info");
            logger.warn("First warn");
            logger.error("First error");
        }, "Multiple log messages should not throw exception");
    }
}
