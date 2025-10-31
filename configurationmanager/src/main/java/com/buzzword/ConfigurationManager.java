package com.buzzword;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Stores the configuration for the application.
 * <p>
 * On construction, retrieves the configuration information from the config file. This information
 * is globally available as a singleton, and is made accessibly through a set of getter functions.
 * @author Ted Bjurlin
 * @version 1.0
 */
public class ConfigurationManager implements DatabaseConfiguration {

    private String databaseConnectionString;
    private Integer minDatabaseConnections;
    private Integer maxDatabaseConnections;
    private String databaseName;

    private final Logger logger = LoggerFactory.getEventLogger();

    private XssSanitizer sanitizer = new XssSanitizerImpl();

    private static ConfigurationManager instance;

    /**
     * Constructs a ConfigurationManager
     * <p>
     * Constructs a configuration instance, reading configuration information from the
     * application config file. The manager first looks for a BRL_CONFIG environmental
     * variable specifying the configuration path. If this is not found, it looks for
     * a config file in the working directory.
     * @throws IOException If the config file fails to parse as a java properties file.
     * @throws IllegalArgumentException If a field in the config file is invalid or missing.
     * @throws FileNotFoundExcepton - If the config file could not be found.
     */
    private ConfigurationManager() throws IOException, IllegalArgumentException {
        Properties prop = new Properties();
        String configPath = System.getenv("BRL_CONFIG");
        if (configPath == null) {
            configPath = "brl.properties";
        }
        logger.info("Looking for configuration file at path " + configPath);
    
        try {
            InputStream stream = new FileInputStream(configPath);
            prop.load(stream);
        } catch (FileNotFoundException e) {
            logger.error("No configuration file found.");
            throw e;
        } catch (IOException e) {
            logger.error("Malformed configuration file.");
            throw e;
        }

        createDatabaseConnectionString(
            prop.getProperty("database.userName"),
            prop.getProperty("database.password"),
            prop.getProperty("database.host"),
            prop.getProperty("database.port")
        );

        setMinDatabaseConnections(prop.getProperty("database.pool.min"));
        setMaxDatabaseConnections(prop.getProperty("database.pool.max"));

        setDatabaseName(prop.getProperty("database.name"));
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
     * @throws IllegalArgumentException if any of the fields are invalid.
     */
    private void createDatabaseConnectionString(String userName, String password, String host, String port) throws IllegalArgumentException {

        if (userName == null) {
            logger.error("Config file is missing required field database.userName.");
            throw new IllegalArgumentException("Database username is missing.");
        }
        if (password == null) {
            logger.error("Config file is missing required field database.password.");
            throw new IllegalArgumentException("Database password is missing.");
        }
        if (host == null) {
            logger.error("Config file is missing required field host.");
            throw new IllegalArgumentException("Host is null.");
        }
        if (port == null) {
            logger.error("Config file is missing required field port.");
            throw new IllegalArgumentException("Port is null.");
        }

        String safeUserName = sanitizer.sanitizeInput(userName);

        if (safeUserName.length() < 1 || safeUserName.length() > 128) {
            logger.error("Database userName length is invalid.");
            throw new IllegalArgumentException("Database username has invalid length.");
        }

        String safePassword = sanitizer.sanitizeInput(password);
        String safeHost = sanitizer.sanitizeInput(host);

        String safePort = sanitizer.sanitizeInput(port);

        try {
            Integer portNum = Integer.parseInt(safePort);
            if (portNum < 1 || portNum > 65535) {
                logger.error("Port number is not in the valid port range.");
                throw new IllegalArgumentException("Invalid port number.");
            }
        }  catch (NumberFormatException e) {
            logger.error("database.port is not a number.");
            throw new IllegalArgumentException("Invalid port number.");
        }


        databaseConnectionString = URLEncoder.encode(String.format(
            "mongodb://%s:%s@%s:%s/?authSource=admin",
            safeUserName,
            safePassword,
            safeHost,
            safePort
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
            
            if (numConnections < 0) {
                logger.error("Number of minimum connections is less than zero.");
                throw new IllegalArgumentException("Invalid minimum number of connections.");
            }

            minDatabaseConnections = numConnections;
        } catch (NumberFormatException e) {
            logger.error("Minimum connections is not a number.");
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
                logger.error("Maximum number of connections is less than one or the minimum number of connections.");
                throw new IllegalArgumentException("Invalid maximum number of connections.");
            }

            minDatabaseConnections = numConnections;
        } catch (NumberFormatException e) {
            logger.error("Maximum number of connections is not a number.");
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

    /**
     * Setter for the database name.
     * @param databaseName the name of the database.
     */
    private void setDatabaseName(String databaseName) {
        String safeDatabaseName = sanitizer.sanitizeInput(databaseName);
        this.databaseName = safeDatabaseName;
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public String getDatabaseName() {
        return databaseName;
    }
}