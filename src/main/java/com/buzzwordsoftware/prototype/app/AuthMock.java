package com.buzzwordsoftware.prototype.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthMock {

    private Logger logger = LogManager.getLogger("com.buzzwordsoftware.prototype.EventLogger");

    private Logger securityLogger = LogManager.getLogger("com.buzzwordsoftware.prototype.SecurityLogger");
 
    public String sendToken(String token) {
        securityLogger.info("Recieved token: {}", token);
        logger.info("Sending token to be decoded.");
        String decodedToken = this.decode(token);
        securityLogger.info("Recieved decoded token: {}", decodedToken);
        return decodedToken;
    }

    private String decode(String token) {
        return "Decoded" + token;
    }
}
