package com.buzzword;

/*
 * This is free and unencumbered software released into the public domain.
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
 * either in source code form or as a compiled binary, for any purpose, commercial or
 * non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public domain.
 * We make this dedication for the benefit of the public at large and to the detriment of
 * our heirs and successors. We intend this dedication to be an overt act of relinquishment in
 * perpetuity of all present and future rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to: https://unlicense.org/
*/

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

    /**
     * Constructs a new Token object with default values.
     */
    public Token() {}

    /**
     * Gets the token.
     * 
     * @return the validated JWT token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     * <p>
     * Sanitizes the token and validates according to the following rules:
     * The token must be between 250 and 400 characters.
     * 
     * @param token the token to be sanitized and validated
     * @throws IllegalArgumentException when validation fails
     */
    public void setToken(String token) throws IllegalArgumentException {

        if (token == null) {
            logger.error("No authentication token received.");
            throw new IllegalArgumentException("No authentication token received.");
        }

        String safeToken = sanitizer.sanitizeInput(token);

        if (safeToken.length() < 250) {
            logger.error("Authentication token received is too short.");
            throw new IllegalArgumentException("JWT token is too short.");
        } else if (safeToken.length() > 400) {
            logger.error("Authentication token received is too long.");
            throw new IllegalArgumentException("JWT token is too long.");
        }

        this.token = safeToken;
    }    
}
