package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoleConfigurationTest {

    @Mock
    ConfigurationManagerImpl manager;
    
    @Test
    void getsRoleMap() {
        List<String> adminRoles = new ArrayList<String>(List.of("User1", "User2"));
        List<String> contributorRoles = new ArrayList<String>(List.of("User3", "User4"));
        List<String> commenterRoles = new ArrayList<String>(List.of("User5", "User6"));

        when(manager.getAdminUserRoles()).thenReturn(adminRoles);
        when(manager.getContributorUserRoles()).thenReturn(contributorRoles);
        when(manager.getCommenterUserRoles()).thenReturn(commenterRoles);

        RoleConfiguration config = new RoleConfigurationImpl(manager);

        Map<String, String> roleMap = config.getRoleMap();

        assertEquals(6, roleMap.size());

        assertEquals("Admin", roleMap.get("User1"));
        assertEquals("Admin", roleMap.get("User2"));
        assertEquals("Contributor", roleMap.get("User3"));
        assertEquals("Contributor", roleMap.get("User4"));
        assertEquals("Commenter", roleMap.get("User5"));
        assertEquals("Commenter", roleMap.get("User6"));
    }
    
    @Test
    void getsEmptyRoleMap() {
        List<String> adminRoles = new ArrayList<String>();
        List<String> contributorRoles = new ArrayList<String>();
        List<String> commenterRoles = new ArrayList<String>();

        when(manager.getAdminUserRoles()).thenReturn(adminRoles);
        when(manager.getContributorUserRoles()).thenReturn(contributorRoles);
        when(manager.getCommenterUserRoles()).thenReturn(commenterRoles);

        RoleConfiguration config = new RoleConfigurationImpl(manager);

        Map<String, String> roleMap = config.getRoleMap();

        assertEquals(0, roleMap.size());
    }
}
