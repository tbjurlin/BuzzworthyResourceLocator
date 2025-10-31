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
import com.fasterxml.jackson.databind.PropertyMetadata;

public class AuthenticationImpl implements Authentication{
    private URL serverUrl;

    public AuthenticationImpl(String urlString) {
        try {
            URI uri = new URI(urlString);
            serverUrl = uri.toURL();
        } catch (URISyntaxException e) {
            System.out.println(e);
        } catch (MalformedURLException f) {
            System.out.println(f);
        }
    }

    public Credentials Authenticate(Token token) {

        try {
            if(serverUrl == null) {
                // Throw exception
                System.out.println("URL is null!");
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
                return userCredentials;
            } else {
                // Throw exception
                System.out.println("Bad response code!");
            }

            connection.disconnect();
        } catch (IOException e) {
            // Throw exception
            System.out.println(e);
        }
        return null;
    }
}
