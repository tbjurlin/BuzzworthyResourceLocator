package com.buzzword;

/*
 * This is free and unencumbered software released into the public domain.
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
 * either in source code form or as a compiled binary, for any purpose, commercial or
 * non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public domain.
 * We make this dedication for the benefit of the public at large and to the detriment of
 * our heirs and successors. We intend this dedication to be an overt act of relinquishment in
 * perpetuity of all present and future rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to: https://unlicense.org/
*/

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
     * Gets the auth server subdomain from the config file. 
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

    /**
     * Gets the about page information from the config file
     * @return the about page information
     */
    public abstract String getAboutPageInfo();
}
