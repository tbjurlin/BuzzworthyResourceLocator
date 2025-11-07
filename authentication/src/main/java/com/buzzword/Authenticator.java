package com.buzzword;

/**
 * Authenticator interface. 
 * This interface facilitates a connection to an authentication server to
 * retrieve user credentials using a JSON Web Token (JWT) provided by
 * the authentication server for Single Sign-On (SSO).
 * 
 * @author Ben Edens
 * @version 1.0
 */
public interface Authenticator {

    /**
     * Send a JSON Web Token (JWT) to an authentication server to be
     * authenticated and return the corresponding user's credentials.
     * 
     * @param token A Token object containing a user's JSON Web Token (JWT) obtained from the authentication server.
     * @return A Credentials object storing the user's credentials.
     */
    Credentials Authenticate(Token token);
}
