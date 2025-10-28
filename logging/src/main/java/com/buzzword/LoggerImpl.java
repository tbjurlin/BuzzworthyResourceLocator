package com.buzzword;

import org.apache.logging.log4j.LogManager;

public class LoggerImpl implements Logger {

    private final org.apache.logging.log4j.Logger logger;

    public LoggerImpl(String loggerName) {
        logger = LogManager.getLogger(loggerName);
    }

    @Override
    public void trace(String message) {
        logger.trace("{}", message);
    }

    @Override
    public void debug(String message) {
        logger.debug("{}", message);
    }

    @Override
    public void info(String message) {
        logger.info("{}", message);
    }

    @Override
    public void warn(String message) {
        logger.warn("{}", message);
    }

    @Override
    public void error(String message) {
        logger.error("{}", message);
    }
    
}
