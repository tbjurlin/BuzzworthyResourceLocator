package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class LoggerFactoryTest {
    
    @Test
    void gettingEventLoggerReturnsSameInstance() {
        Logger instance1 = LoggerFactory.getEventLogger();
        Logger instance2 = LoggerFactory.getEventLogger();

        assertEquals(instance1, instance2);
    }
    
    @Test
    void gettingSecurityLoggerReturnsSameInstance() {
        Logger instance1 = LoggerFactory.getSecurityLogger();
        Logger instance2 = LoggerFactory.getSecurityLogger();

        assertEquals(instance1, instance2);
    }
    
    @Test
    void securityAndEventLoggersReturnDifferentInstances() {
        Logger instance1 = LoggerFactory.getEventLogger();
        Logger instance2 = LoggerFactory.getSecurityLogger();

        assertNotEquals(instance1, instance2);
    }

    @Test
    void getEventLoggerReturnsNotNull() {
        Logger logger = LoggerFactory.getEventLogger();
        assertNotNull(logger, "Event logger should not be null");
    }

    @Test
    void getSecurityLoggerReturnsNotNull() {
        Logger logger = LoggerFactory.getSecurityLogger();
        assertNotNull(logger, "Security logger should not be null");
    }
}
