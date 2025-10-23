package com.buzzwordsoftware.prototype.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    protected static final Logger logger = LogManager.getLogger("com.buzzwordsoftware.prototype.EventLogger");
    
    public static void main(String[] args) {
        logger.info("App started");
        AuthMock authServer = new AuthMock();
        logger.info("Connected to auth server.");
        APIMock api = new APIMock(authServer);
        logger.info("Launched API");

        api.processRequest("Web token", "Here be dragons!");
    }
}
