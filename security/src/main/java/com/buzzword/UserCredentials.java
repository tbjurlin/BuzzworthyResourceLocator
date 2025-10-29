/**
 * This is the primary Interface for handling User Credentials.
 * 
 * @author Janniebeth Melendez
 * @version 1.0
 */
package com.buzzword;

public interface UserCredentials {

    /**
     * Return the String[] containing the roles for the UserCredentials
     * 
     * @return String[] holding the roles for the given User
     */
    String[] getRoles();

    /**
     * Check if a specific Role is in the list for UserCredentials.
     * i.e. Employee (General User), Developer (Contributor), and Manager (Admin)
     * 
     * @param role what we're checking for
     * @return true if the Role is in the list of roles
     * 
     */
    boolean hasRole(String role);
}