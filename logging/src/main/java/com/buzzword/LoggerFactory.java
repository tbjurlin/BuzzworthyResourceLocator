package com.buzzword;

public class LoggerFactory {

    private static Logger securityLogger;
    private static Logger eventLogger;    

    public static Logger getSecurityLogger() {
        if (securityLogger == null) {
            securityLogger = new LoggerImpl("com.buzzword.SecurityLogger");
        }
        return securityLogger;
    }

    public static Logger getEventLogger() {
        if (eventLogger == null) {
            eventLogger = new LoggerImpl("com.buzzword.EventLogger");
        }
        return eventLogger;
    }
}
