package com.buzzword;

import java.util.List;

public interface ConfigurationManager {
    
    /**
     * Gets the name of the database user from the config file.
     * @return the name of the database user
     */
    public abstract String getDatabaseUserName();
    
    /**
     * Gets the password of the database user from the config file.
     * @return the password of the database user
     */
    public abstract String getDatabasePassword();
    
    /**
     * Gets the host address of the database from the config file.
     * @return the host address of the database
     */
    public abstract String getDatabaseHost();
    
    /**
     * Gets the port of the database from the config file.
     * @return the port of the database
     */
    public abstract String getDatabasePort();
    
    /**
     * Gets the minimum number of the connections in the database connection pool from the config file.
     * @return the minimum connection pool size
     */
    public abstract String getDatabaseMinPoolSize();
    
    /**
     * Gets the maximum number of connections in the database connection pool from the config file.
     * @return the maximum connection pool size
     */
    public abstract String getDatabaseMaxPoolSize();

    /**
     * Gets the database name from the config file.
     * @return the database name
     */
    public abstract String getDatabaseName();

    /**
     * Gets the auth server hostname from the config file.
     * @return the auth server url
     */
    public abstract String getAuthServerHost();

    /**
     * Gets the auth server port from the config file. 
     * @return the auth server port
     */
    public abstract String getAuthServerPort();

    /**
     * Gets the auth server subdomin from the config file. 
     * @return the auth server subdomain
     */
    public abstract String getAuthServerSubdomain();

    /**
     * Gets the list of user roles mapped to Admin
     * @return the list of user roles
     */
    public abstract List<String> getAdminUserRoles();

    /**
     * Gets the list of user roles mapped to Contributor
     * @return the list of user roles
     */
    public abstract List<String> getContributorUserRoles();

    /**
     * Gets the list of user roles mapped to Commenter
     * @return the list of user roles
     */
    public abstract List<String> getCommenterUserRoles();
}
