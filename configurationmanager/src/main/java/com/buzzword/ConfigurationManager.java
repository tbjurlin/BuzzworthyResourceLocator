package com.buzzword;

public class ConfigurationManager implements DatabaseConfiguration {

    private String userName;
    private String password;
    private String host;
    private String port;
    private String databaseName;

    private String databaseConnectionString;

    private static ConfigurationManager instance;

    private ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }

        return instance;
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public String getDatabaseConnectionString() {
        return databaseConnectionString;
    }
}
