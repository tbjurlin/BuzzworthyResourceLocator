package com.buzzword;

import java.util.Map;

/**
 * An interface for managaing the configuration of the system roles.
 * 
 * @author Ted Bjurlin
 * @version 1.0
 */
public interface RoleConfiguration {

    /**
     * Gets a map that maps user roles to their system role.
     * <p>
     * The keys in tis map are the user roles provided by the auth server.
     * The values are the corresponding system role.
     * @return user to system role map
     */
    public Map<String, String> getRoleMap();
}
