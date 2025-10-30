package com.buzzword;

/**
 * The database configuration information.
 * <p>
 * Retreives information from the configuration files and constructs a database connection string.
 * @author Ted Bjurlin
 */
public interface DatabaseConfiguration {

    /**
     * Getter for the database connection string for the current database implementation.
     * @return database connection string
     */
    public abstract String getDatabaseConnectionString();
}
