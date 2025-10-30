package com.buzzword;

/**
 * Factory class to provide access to Loggers.
 * <p>
 * Provides access to the implementations of the security and event loggers.
 * 
 * @author Ted Bjurlin
 */
public class LoggerFactory {

    /**
     * The logger instance that logs security events.
     */
    private static Logger securityLogger;

    /**
     * The logger instance that logs application events.
     */
    private static Logger eventLogger;    

    /**
     * Provides singleton access to the securiy logger.
     * @return The logger instance for security.
     */
    public static Logger getSecurityLogger() {
        if (securityLogger == null) {
            securityLogger = new LoggerImpl("com.buzzword.SecurityLogger");
        }
        return securityLogger;
    }

    /**
     * Provides singleton access to the event logger.
     * @return The logger instance for application events.
     */
    public static Logger getEventLogger() {
        if (eventLogger == null) {
            eventLogger = new LoggerImpl("com.buzzword.EventLogger");
        }
        return eventLogger;
    }
}
