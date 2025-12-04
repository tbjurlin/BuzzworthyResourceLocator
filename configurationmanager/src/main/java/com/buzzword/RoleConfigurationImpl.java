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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager for role configuration.
 * @author Ted Bjurlin
 * @version 1.0
 */
public class RoleConfigurationImpl implements RoleConfiguration {

    private Map<String, String> roleMap;

    private Logger logger = LoggerFactory.getSecurityLogger();

    /**
     * Constructs a role configuration using the configuration manager.
     * @param manager the configuration manager containing role information
     */
    public RoleConfigurationImpl(ConfigurationManager manager) {
        createRoleMap(
            manager.getAdminUserRoles(), 
            manager.getContributorUserRoles(),
            manager.getCommenterUserRoles());
    }

    /**
     * Creates a role map from lists of roles.
     * @param adminRoles The list of roles mapped to admin.
     * @param contributorRoles The list of roles mapped to contributor.
     * @param commenterRoles The list of roles mapped to commenter.
     */
    public void createRoleMap(
        List<String> adminRoles,
        List<String> contributorRoles,
        List<String> commenterRoles) {

            Map<String, String> roleMap = new HashMap<String, String>();
            
            if (adminRoles.isEmpty()) {
                logger.warn("No Admin roles defined!");
            }
            if (contributorRoles.isEmpty()) {
                logger.warn("No Contributor roles defined!");
            }
            if (commenterRoles.isEmpty()) {
                logger.warn("No Commenter roles defined!");
            }

            for (String adminRole : adminRoles) {
                roleMap.put(adminRole, "Admin");
            }
            for (String contributorRole : contributorRoles) {
                roleMap.put(contributorRole, "Contributor");
            }
            for (String commenterRole : commenterRoles) {
                roleMap.put(commenterRole, "Commenter");
            }

            this.roleMap = roleMap;
            logger.info(String.format("Created user role mappings: %s", roleMap));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getRoleMap() {
        return roleMap;
    }
    
}
