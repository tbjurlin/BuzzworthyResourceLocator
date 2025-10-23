package com.buzzwordsoftware.prototype.app;

import com.buzzwordsoftware.prototype.app.logging.Logger;
import com.buzzwordsoftware.prototype.app.logging.LoggerImpl;

/**
 * Hello world!
 */
public class App {

    protected static final Logger LOGGER = LoggerImpl.INSTANCE;
    
    public static void main(String[] args) {
        LOGGER.info("Hello Log!");
        System.out.println("Hello World!");
    }
}
