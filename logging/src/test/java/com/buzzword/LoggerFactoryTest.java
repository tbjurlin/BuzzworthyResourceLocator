package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class LoggerFactoryTest {
    
    @Test
    void gettingEventLoggerReturnsSameInstance() {
        Logger instance1 = LoggerFactory.getEventLogger();
        Logger instnace2 = LoggerFactory.getEventLogger();

        assertEquals(instance1, instnace2);
    }
    
    @Test
    void gettingSecuirtyLoggerReturnsSameInstance() {
        Logger instance1 = LoggerFactory.getSecurityLogger();
        Logger instnace2 = LoggerFactory.getSecurityLogger();

        assertEquals(instance1, instnace2);
    }
    
    @Test
    void securityAndEventLoggersReturnDifferentInstances() {
        Logger instance1 = LoggerFactory.getEventLogger();
        Logger instnace2 = LoggerFactory.getSecurityLogger();

        assertNotEquals(instance1, instnace2);
    }
}
