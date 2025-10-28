package com.buzzword;

public class LoggerFactory {
    public static Logger getSecurityLogger() {
        return new LoggerImpl("<security-logger-name>");
    }

    public static Logger getEventLogger() {
        return new LoggerImpl("<event-logger-name>");
    }
}
