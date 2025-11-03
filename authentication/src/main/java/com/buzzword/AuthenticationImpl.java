package com.buzzword;

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
 * Implementation for Authentication.java.
 * This class facilitates a connection to an authentication server to
 * retrieve user credentials using a Java Web Token (JWT) provided by
 * the authorization server for Single Sign-On (SSO).
 * 
 * @author Ben Edens
 * @version 1.0
 */
public class AuthenticationImpl implements Authentication{
    private URL serverUrl;

    /**
     * Authentication implementation constructor. Builds authentication server URL from provided string.
     * 
     * @param urlString A string version of the authentication server's URL.
     */
    public AuthenticationImpl(String urlString) {
        try {
            URI uri = new URI(urlString);
            serverUrl = uri.toURL();
        } catch (URISyntaxException e) {
            throw new AuthenticationException("Cannot construct authentication server uri from provided String due to improper syntax.");
        } catch (MalformedURLException f) {
            throw new AuthenticationException("Cannot construct authentication server url from provided uri due to improper syntax.");
        }
    }

    /**
     * Send a Java Web Token (JWT) to an authentication server to be
     * authenticated and return the corresponding user's credentials.
     * 
     * @param token A Token object containing a user's Java Web Token (JWT) obtained from the authentication server.
     * @return A Credentials object storing the user's credentials.
     */
    public Credentials Authenticate(Token token) {

        try {
            if(serverUrl == null) {
                throw new AuthenticationException("Null server url. Class instance improperly constructed.");
            }
            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            String tokenJson = String.format("{\"token\": \"%s\"}", token.getToken());
            try (OutputStream os = connection.getOutputStream()) {
                byte[] output = (tokenJson).getBytes("utf-8");
                os.write(output, 0, output.length);
            }
            int responseCode = connection.getResponseCode();
            System.out.println("\nResponse Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
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
                return userCredentials;
            } else {
                connection.disconnect();
                String errorMsg = String.format("Received response code %d from authentication server.", responseCode);
                throw new AuthenticationException(errorMsg);
            }
        } catch (IOException e) {
            throw new AuthenticationException("Error connecting to authentication server.");
        }
    }
}