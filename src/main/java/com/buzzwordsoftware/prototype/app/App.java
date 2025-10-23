package com.buzzwordsoftware.prototype.app;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Hello world!
 */
public class App {

    protected static final Logger LOGGER = LogManager.getLogger();
    
    public static void main(String[] args) {
        LOGGER.info("Hello Log!");
        System.out.println("Hello World!");
    }
}
