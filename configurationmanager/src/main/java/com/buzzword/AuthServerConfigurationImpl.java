package com.buzzword;

/**
 * Stores the configuration for the application.
 * <p>
 * On construction, retrieves the configuration information from the config file. This information
 * is globally available as a singleton, and is made accessibly through a set of getter functions.
 * @author Ted Bjurlin
 * @version 1.0
 */
public class AuthServerConfigurationImpl implements AuthServerConfiguration {

    private String authServerConnectionString;

    private final Logger logger = LoggerFactory.getEventLogger();

    private XssSanitizer sanitizer = new XssSanitizerImpl();

    /**
     * Constructs a AuthServerConfiguration using ConfigurationManager
     * <p>
     * Constructs a auth server configuration, reading configuration information from the
     * configuration manager. 
     * @throws ConfigurationException If a field in the config file is invalid or missing.
     */
    public AuthServerConfigurationImpl(ConfigurationManager manager) {
        
        createAuthServerConnectionString(
            manager.getAuthServerHost(),
            manager.getAuthServerPort()
        );
    }

    /**
     * Create an auth server connection string.
     * @param host the host URL of the auth server.
     * @param port the port the auth server is hosted on.
     * @throws ConfigurationException if any of the fields are invalid.
     */
    private void createAuthServerConnectionString(String host, String port) {

        if (host == null) {
            logger.error("Config file is missing required field host.");
            throw new ConfigurationException("Host is null.");
        }
        if (host == "") {
            logger.error("Database host is empty.");
            throw new ConfigurationException("Database host is empty.");
        }

        String safeHost = sanitizer.sanitizeInput(host);

        if (port == null) {
            authServerConnectionString = safeHost;
        } else {
            try {
                Integer portNum = Integer.parseInt(port);
                if (portNum < 1 || portNum > 65535) {
                    logger.error("Port number is not in the valid port range.");
                    throw new ConfigurationException("Invalid port number.");
                }
                authServerConnectionString = String.format(
                    "%s:%d",
                    safeHost,
                    portNum
                );
            }  catch (NumberFormatException e) {
                logger.error("database.port is not a number.");
                throw new ConfigurationException("Invalid port number.");
            }
        }



    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthServerConnectionString() {
        return authServerConnectionString;
    }
}