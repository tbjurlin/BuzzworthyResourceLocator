package com.buzzword;

/**
 * Represents a JSON Web Token.
 * <p>
 * This class provides formatting validation for JWTs prior to sending to
 * the Authentication Server.
 * 
 * @author Ted Bjurlin
 * @version 1.0
 */
public class Token {

    /**
     * A reference to the security logger to log if token validation fails.
     */
    Logger logger = LoggerFactory.getSecurityLogger();

    /**
     * The sanitizer used to prevent XSS attacks.
     */
    XssSanitizer sanitizer = new XssSanitizerImpl();

    /**
     * Represents the validated token string provided by the user.
     */
    private String token;

    public Token() {}

    /**
     * Getter for the token.
     * 
     * @return validated JWT token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter for the token.
     * <p>
     * Sanitizes token and validates according to the following rules:
     * The token must be between 250 and 400 characters.
     * <p>
     * @param token The token to be sanitized and validated.
     * @throws IllegalArgumentException when validation fails.
     */
    public void setToken(String token) throws IllegalArgumentException {

        if (token == null) {
            logger.error("No authentication token recieved.");
            throw new IllegalArgumentException("No authentication token recieved.");
        }

        String safeToken = sanitizer.sanitizeInput(token);

        if (safeToken.length() < 250) {
            logger.error("Authentication token recieved is too short.");
            throw new IllegalArgumentException("JWT token is too short.");
        } else if (safeToken.length() > 400) {
            logger.error("Authentication token recieved is too long.");
            throw new IllegalArgumentException("JWT token is too long.");
        }

        this.token = safeToken;
    }    
}
