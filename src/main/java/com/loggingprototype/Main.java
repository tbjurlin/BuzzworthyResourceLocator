package com.loggingprototype;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    protected static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        System.out.println("Hello world!");

        logger.info("Hello log!");
    }
}