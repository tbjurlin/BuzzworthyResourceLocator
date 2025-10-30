package com.buzzword;

import org.apache.logging.log4j.LogManager;

/**
 * Implementation of {@link Logger} using Log4J.
 * <p>
 * Logs messages to the Log4J logger using the same log
 * levels as defined in {@link Logger}.
 * 
 * @author Ted Bjurlin
 */
public class LoggerImpl implements Logger {

    /**
     * The Log4J logger instance to log messages to.
     */
    private final org.apache.logging.log4j.Logger logger;

    /**
     * Creates a LoggerImpl that logs messages to the named Log4J logger.
     * @param loggerName the name of the logger to send log messages to.
     */
    public LoggerImpl(String loggerName) {
        logger = LogManager.getLogger(loggerName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(String message) {
        logger.trace("{}", message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String message) {
        logger.debug("{}", message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String message) {
        logger.info("{}", message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String message) {
        logger.warn("{}", message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String message) {
        logger.error("{}", message);
    }
    
}
