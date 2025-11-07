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

import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation for Authenticator.java.
 * This class facilitates a connection to an authentication server to
 * retrieve user credentials using a JSON Web Token (JWT) provided by
 * the authentication server for Single Sign-On (SSO).
 * 
 * @author Ben Edens
 * @version 1.0
 */
public class AuthenticatorImpl implements Authenticator{

    private URL serverUrl;
    private final Logger logger = LoggerFactory.getEventLogger();

    /**
     * Authenticator implementation constructor. Builds authentication server URL from provided string.
     * 
     * @param urlString A string version of the authentication server's URL.
     */
    public AuthenticatorImpl(String urlString) {
        try {
            URI uri = new URI(urlString);
            serverUrl = uri.toURL();
        } catch (URISyntaxException e) {
            logger.error("Cannot construct authentication server uri from provided String due to improper syntax.");
            throw new AuthenticationException("Cannot construct authentication server uri from provided String due to improper syntax.");
        } catch (MalformedURLException f) {
            logger.error("Cannot construct authentication server url from provided uri due to improper syntax.");
            throw new AuthenticationException("Cannot construct authentication server url from provided uri due to improper syntax.");
        }
    }

    /**
     * Send a JSON Web Token (JWT) to an authentication server to be
     * authenticated and return the corresponding user's credentials.
     * 
     * @param token A Token object containing a user's JSON Web Token (JWT) obtained from the authentication server.
     * @return A Credentials object storing the user's credentials.
     */
    public Credentials Authenticate(Token token) {
        logger.info("Authenticating token.");
        try {
            if(serverUrl == null) {
                logger.error("Null server url. Authenticator class instance improperly constructed.");
                throw new AuthenticationException("Null server url. Class instance improperly constructed.");
            }
            if(token == null) {
                logger.error("Null token provided: cannot authenticate.");
                throw new AuthenticationException("Null token provided: cannot authenticate.");
            }
            logger.info("Setting up connection to external authentication server.");
            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            String tokenJson = String.format("{\"token\": \"%s\"}", token.getToken());
            logger.info("Attempting to send data to authentication server.");
            try (OutputStream os = connection.getOutputStream()) {
                byte[] output = (tokenJson).getBytes("utf-8");
                os.write(output, 0, output.length);
            }
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                logger.info("Received HTTP response code 201 from authentication server.");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String responseStr = response.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                Credentials userCredentials;
                userCredentials = objectMapper.readValue(responseStr, Credentials.class);
                connection.disconnect();
                if(userCredentials == null) {
                    logger.error("Authenticator must not return null credentials");
                    throw new AuthenticationException("Authenticator must not return null credentials.");
                }
                logger.info("Successfully received credentials from authentication server.");
                return userCredentials;
            } else {
                connection.disconnect();
                String errorMsg = String.format("Received response code %d from authentication server.", responseCode);
                logger.error(errorMsg);
                throw new AuthenticationException(errorMsg);
            }
        } catch (IOException e) {
            logger.error("Error connecting to authentication server.");
            throw new AuthenticationException("Error connecting to authentication server.");
        }
    }
}