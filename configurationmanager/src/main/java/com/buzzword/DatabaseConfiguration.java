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

    /**
     * Getter for the database name.
     * @return database name
     */
    public abstract String getDatabaseName();

    /**
     * Getter for the minimum number of connections in the database connection pool.
     * @return min database pool connections
     */
    public abstract Integer getMinDatabaseConnections();

    /**
     * Getter for the maximum number of connections in the database connection pool.
     * @return max database pool connections
     */
    public abstract Integer getMaxDatabaseConnections();
}
