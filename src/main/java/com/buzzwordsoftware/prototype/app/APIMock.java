package com.buzzwordsoftware.prototype.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class APIMock {

    private AuthMock authServer;
    private Logger logger = LogManager.getLogger("com.buzzwordsoftware.prototype.EventLogger");

    public APIMock(AuthMock authServer) {
        this.authServer = authServer;
    }

    public void processRequest(String token, String message) {
        logger.info("Recieved message: {}", message);
        logger.info("Sending token to server.");
        String response = authServer.sendToken(token);
        logger.info("Recieved response from server: {}", response);
    }
    
}