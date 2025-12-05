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

/**
 * Stores the configuration for the application.
 * <p>
 * On construction, retrieves the configuration information from the config file. This information
 * is globally available as a singleton, and is made accessibly through a set of getter functions.
 * @author Ted Bjurlin
 * @version 1.0
 */
public class AuthServerConfigurationImpl implements AuthServerConfiguration {

    private String authServerConnectionString;

    private final Logger logger = LoggerFactory.getEventLogger();

    private XssSanitizer sanitizer = new XssSanitizerImpl();

    /**
     * Constructs an AuthServerConfiguration using ConfigurationManager.
     * <p>
     * Constructs an auth server configuration, reading configuration information from the
     * configuration manager. 
     * @param manager the configuration manager containing auth server information
     * @throws ConfigurationException if a field in the config file is invalid or missing
     */
    public AuthServerConfigurationImpl(ConfigurationManager manager) {
        
        createAuthServerConnectionString(
            manager.getAuthServerHost(),
            manager.getAuthServerPort(),
            manager.getAuthServerSubdomain()
        );
    }

    /**
     * Creates an auth server connection string.
     * @param host the host URL of the auth server
     * @param port the port the auth server is hosted on
     * @param subdomain the subdomain path of the auth server
     * @throws ConfigurationException if any of the fields are invalid
     */
    private void createAuthServerConnectionString(String host, String port, String subdomain) {

        if (host == null) {
            logger.error("Config file is missing required field host.");
            throw new ConfigurationException("Host is null.");
        }
        if (host == "") {
            logger.error("Database host is empty.");
            throw new ConfigurationException("Database host is empty.");
        }
        if (subdomain == null) {
            logger.error("Config file is missing required field subdomain.");
            throw new ConfigurationException("Subdomain is null.");
        }
        if (subdomain == "") {
            logger.error("Database subdomain is empty.");
            throw new ConfigurationException("Database subdomain is empty.");
        }

        String safeHost = sanitizer.sanitizeInput(host);
        String safeSubdomain = sanitizer.sanitizeInput(subdomain);

        if (port == null) {
            authServerConnectionString = String.format("%s%s", safeHost, safeSubdomain);
        } else {
            try {
                Integer portNum = Integer.parseInt(port);
                if (portNum < 1 || portNum > 65535) {
                    logger.error("Port number is not in the valid port range.");
                    throw new ConfigurationException("Invalid port number.");
                }
                authServerConnectionString = String.format(
                    "%s:%d%s",
                    safeHost,
                    portNum,
                    safeSubdomain
                );
            }  catch (NumberFormatException e) {
                logger.error("database.port is not a number.");
                throw new ConfigurationException("Invalid port number.");
            }
        }



    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthServerConnectionString() {
        return authServerConnectionString;
    }
}