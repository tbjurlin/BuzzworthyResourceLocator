package com.buzzword;

/**
 * The authentication server configuration information.
 * <p>
 * Retreives information from the configuration files and constructs an auth server connection string.
 * @author Ted Bjurlin
 */
public interface AuthServerConfiguration {

    /**
     * Getter for the auth server connection string for the current auth server implementation.
     * @return authentication server connection string
     */
    public abstract String getAuthServerConnectionString();
}
