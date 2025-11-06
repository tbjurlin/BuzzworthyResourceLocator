package com.buzzword;

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
     * @param manager
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
