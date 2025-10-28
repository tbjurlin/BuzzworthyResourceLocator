package com.buzzword;

import org.apache.logging.log4j.LogManager;

public class LoggerImpl implements Logger {

    private final org.apache.logging.log4j.Logger logger;

    public LoggerImpl(String loggerName) {
        this.logger = LogManager.getLogger(loggerName);
    }

    @Override
    public void trace(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trace'");
    }

    @Override
    public void debug(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debug'");
    }

    @Override
    public void info(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'info'");
    }

    @Override
    public void warn(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'warn'");
    }

    @Override
    public void error(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'error'");
    }
    
}
