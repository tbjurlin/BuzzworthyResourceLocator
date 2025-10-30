package com.buzzword;

/**
 * Interface for accessing logging.
 * <p>
 * Provides methods to produce logs with the following levels:
 * <ul>
 * <li>Trace - Highly granular, only for debugging.</li>
 * <li>Debug - High level debugging messages.</li>
 * <li>Info - Level for standard events.</li>
 * <li>Warn - Level for potential issues.</li>
 * <li>Error - Level for actual issues.</li>
 * </ul>
 * <p>
 * Messages must be Strings.
 * @author Ted Bjurlin
 */
public interface Logger {

    /**
     * Logs a message at the trace level.
     * <p>
     * Used for highly granular logging during debugging. Disable in production.
     * @param message message to send to the log.
     */
    public abstract void trace(String message);

    /**
     * Logs a message at the debug level.
     * <p>
     * Used for less detailed logging in debugging. Ex. Identifying which functions
     * are called, which if/else branches are followed. Disable in production.
     * @param message message to send to the log.
     */
    public abstract void debug(String message);
    
    /**
     * Logs a message at the info level.
     * <p>
     * Used for the logging of events that occur in the normal execution of the program.
     * @param message message to send to the log.
     */
    public abstract void info(String message);

    /**
     * Logs a message at the warn level.
     * <p>
     * Used for the logging of potential problems or risks that do not impact the immediate
     * ability of the program to function.
     * @param message message to send to the log.
     */
    public abstract void warn(String message);

    /**
     * Logs a message at the error level.
     * <p>
     * Used for the logging of errors that arise during the execution of the program.
     * @param message message to send to the log.
     */
    public abstract void error(String message);
}
