package com.buzzword;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Stores the configuration for the application.
 * <p>
 * On construction, retrieves the configuration information from the config file. This information
 * is globally available as a singleton, and is made accessibly through a set of getter functions.
 * @author Ted Bjurlin
 * @version 1.0
 */
public class ConfigurationManagerImpl implements ConfigurationManager {

    private Properties propertiesFile = new Properties();

    private final Logger logger = LoggerFactory.getEventLogger();

    private static ConfigurationManagerImpl instance;

    /**
     * Constructs a ConfigurationManager
     * <p>
     * Constructs a configuration instance, reading configuration information from the
     * application config file. The manager first looks for a BRL_CONFIG environmental
     * variable specifying the configuration path. If this is not found, it looks for
     * a config file in the working directory.
     * @throws ConfigurationException if there is a configuration loading error
     */
    private ConfigurationManagerImpl()  {
        String configPath = System.getenv("BRL_CONFIG");
        if (configPath == null) {
            configPath = "brl.properties";
        }
        logger.info("Looking for configuration file at path " + configPath);
    
        try {
            InputStream stream = new FileInputStream(configPath);
            propertiesFile.load(stream);
        } catch (FileNotFoundException e) {
            logger.error("No configuration file found.");
            throw new ConfigurationException("No configuration file found.");
        } catch (IOException e) {
            logger.error("Malformed configuration file.");
            throw new ConfigurationException("Malformed configuration file.");
        }
    }

    /**
     * Gets a reference to the ConfigurationManager.
     * @return a reference to the singleton instance of the ConfigurationManager
     */
    public static ConfigurationManagerImpl getInstance() {
        if (instance == null) {
            instance = new ConfigurationManagerImpl();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatabaseUserName() {
        return propertiesFile.getProperty("database.userName");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatabasePassword() {
        return propertiesFile.getProperty("database.password");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatabaseHost() {
        return propertiesFile.getProperty("database.host");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatabasePort() {
        return propertiesFile.getProperty("database.port");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatabaseMinPoolSize() {
        return propertiesFile.getProperty("database.pool.min");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatabaseMaxPoolSize() {
        return propertiesFile.getProperty("database.pool.min");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDatabaseName() {
        return propertiesFile.getProperty("database.name");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthServerHost() {
        return propertiesFile.getProperty("authentication.url");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthServerPort() {
        return propertiesFile.getProperty("authentication.port");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthServerSubdomain() {
        return propertiesFile.getProperty("authentication.subdomain");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAdminUserRoles() {
        List<String> roles = new ArrayList<String>();
        int idx = 0;
        String property = propertiesFile.getProperty("roles.admin.0");
        while (property != null) {
            roles.add(property);
            idx++;
            property = propertiesFile.getProperty("roles.admin." + idx);
        }
        return roles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getContributorUserRoles() {
        List<String> roles = new ArrayList<String>();
        int idx = 0;
        String property = propertiesFile.getProperty("roles.contributor.0");
        while (property != null) {
            roles.add(property);
            idx++;
            property = propertiesFile.getProperty("roles.contributor." + idx);
        }
        return roles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getCommenterUserRoles() {
        List<String> roles = new ArrayList<String>();
        int idx = 0;
        String property = propertiesFile.getProperty("roles.commenter.0");
        while (property != null) {
            roles.add(property);
            idx++;
            property = propertiesFile.getProperty("roles.commenter." + idx);
        }
        return roles;
    }
}