package com.buzzword;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Stores the configuration for the application.
 * <p>
 * On construction, retrieves the configuration information from the config file. This information
 * is globally available as a singleton, and is made accessibly through a set of getter functions.
 * @author Ted Bjurlin
 */
public class ConfigurationManager implements DatabaseConfiguration {

    private String databaseConnectionString;
    private Integer minDatabaseConnections;
    private Integer maxDatabaseConnections;

    private static ConfigurationManager instance;

    private ConfigurationManager() throws IOException, IllegalArgumentException {
        Properties prop = new Properties();
        InputStream stream = ConfigurationManager.class.getResourceAsStream("/config.properties");
        prop.load(stream);

        createDatabaseConnectionString(
            prop.getProperty("database.userName"),
            prop.getProperty("database.password"),
            prop.getProperty("database.host"),
            prop.getProperty("database.port"),
            prop.getProperty("database.databaseName")
        );

        setMinDatabaseConnections(prop.getProperty("database.pool.min"));
        setMaxDatabaseConnections(prop.getProperty("database.pool.max"));
    }

    /**
     * Gets a reference to the ConfigurationManager.
     * @return a reference to the singleton instance of the ConfigurationManager
     * @throws IllegalArgumentException if there is an invalid field in the config file.
     * @throws IOException if the config file does not exist.
     */
    public static ConfigurationManager getInstance() throws IllegalArgumentException, IOException {
        if (instance == null) {
            instance = new ConfigurationManager();
        }

        return instance;
    }

    /**
     * Create a database connection string.
     * <p>
     * Creates a standard URL encoded database connection string for a standalone, self-hosted MongoDB database.
     * @param userName the name of the database user.
     * @param password the password of the database user.
     * @param host the host URL of the database.
     * @param port the port the databse is hosted on.
     * @param databaseName the name of the database.
     * @throws IllegalArgumentException if any of the fields are invalid.
     */
    private void createDatabaseConnectionString(String userName, String password, String host, String port, String databaseName) throws IllegalArgumentException {

        if (userName == null) {
            throw new IllegalArgumentException("Database username is missing.");
        }
        if (password == null) {
            throw new IllegalArgumentException("Database password is missing.");
        }
        if (host == null) {
            throw new IllegalArgumentException("Host is null.");
        }
        if (port == null) {
            throw new IllegalArgumentException("Port is null.");
        }
        if (databaseName == null) {
            throw new IllegalArgumentException("Databse name is null");
        }

        // TODO: Add sanitization.
        String safeUserName = userName;

        if (safeUserName.length() < 1 || safeUserName.length() > 128) {
            throw new IllegalArgumentException("Database username either missing or invalid.");
        }

        // TODO: Add sanitization.
        String safePassword = password;
        String safeHost = host;

        String safePort = port;

        try {
            Integer portNum = Integer.parseInt(safePort);
            if (portNum < 1 || portNum > 65535) {
                throw new IllegalArgumentException("Invalid host number.");
            }
        }  catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid host number.");
        }

        String safeDatabaseName = databaseName;


        databaseConnectionString = URLEncoder.encode(String.format(
            "mongodb://%s:%s@%s:%s/%s?authSource=admin",
            safeUserName,
            safePassword,
            safeHost,
            safePort,
            safeDatabaseName
        ), StandardCharsets.UTF_8);
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public String getDatabaseConnectionString() {
        return databaseConnectionString;
    }

    /**
     * Sets the minimum number of connections in the database connection pool.
     * @param connections the number of connections as a String
     */
    private void setMinDatabaseConnections(String connections) {
        try {
            Integer numConnections = Integer.parseInt(connections);
            
            if (numConnections < 0 || numConnections > 100) {
                throw new IllegalArgumentException("Invalid minimum number of connections.");
            }

            minDatabaseConnections = numConnections;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid minimum number of connections.");
        }
    }

    /**
     * Sets the maximum number of connections in the database connection pool.
     * @param connections the number of connections as a String
     */
    private void setMaxDatabaseConnections(String connections) {
        try {
            Integer numConnections = Integer.parseInt(connections);
            
            if (numConnections < 1 || numConnections < minDatabaseConnections) {
                throw new IllegalArgumentException("Invalid maximum number of connections.");
            }

            minDatabaseConnections = numConnections;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid maximum number of connections.");
        }
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public Integer getMinDatabaseConnections() {
        return minDatabaseConnections;
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public Integer getMaxDatabaseConnections() {
        return maxDatabaseConnections;
    }
}