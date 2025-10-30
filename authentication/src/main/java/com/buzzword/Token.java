package com.buzzword;

/**
 * Represents a JSON Web Token.
 * This class provides formatting validation for JWTs prior to sending to
 * the Authentication Server.
 * 
 * @author Ted Bjurlin
 */
public class Token {

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
     * // TODO: Identify min and max length.
     * The token is formed of three sections separated by periods:
     *      the header, the payload, and the signature.
     * Each section may only contain the characters a-zA-Z0-9_-.
     * <p>
     * @param token The token to be sanitized and validated.
     * @throws IllegalArgumentException when validation fails.
     */
    public void setToken(String token) {
        // TODO: Add sanitization.
        String safeToken = token;

        // TODO: Set up checks for JWT token max and min size.

        // Ensure token follows valid JWT format
        // [\\w-] matches one character fron a-zA-Z0-9_-
        // The regex applies this to one or more characters, a period, one or more characters,
        //      a period, one or more characters. On discussion with Jonathon, we can refine this.
        if (!safeToken.matches("[\\w-]+\\.[\\w-]+\\.[\\w-]+")) {
            throw new IllegalArgumentException("JWT token contained invalid characters");
        }

        this.token = safeToken;
    }    
}
