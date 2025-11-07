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
