package com.buzzword;

public class LoggerFactory {

    private static Logger securityLogger;
    private static Logger eventLogger;    

    public static Logger getSecurityLogger() {
        if (securityLogger == null) {
            securityLogger = new LoggerImpl("<security-logger-name>");
        }
        return securityLogger;
    }

    public static Logger getEventLogger() {
        if (eventLogger == null) {
            eventLogger = new LoggerImpl("<event-logger-name>");
        }
        return eventLogger;
    }
}
