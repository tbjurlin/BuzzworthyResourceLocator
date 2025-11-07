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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;

public class CredentialsTest {

    private Credentials testCredentials = null;
    
    @BeforeEach
    void setUp() {
        testCredentials = new Credentials();
    }

    @Test
    public void testValidId() {
        testCredentials.setId(1);
        assertEquals(1, testCredentials.getId()); 
    }

    @Test
    public void testValidZeroId() {
        testCredentials.setId(0);
        assertEquals(0, testCredentials.getId()); 
    }

    @Test
    public void testInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCredentials.setId(-1);
        }); 
    }

    @Test
    public void testValidTitle() {
        String[] titles = {"Developer", "Manager"};
        for (String title : titles) {
            testCredentials.setTitle(title);
            assertEquals(title, testCredentials.getTitle());
        }
    }

    @Test
    public void testLongTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            String longTitle = RandomStringUtils.insecure().next(65);
            testCredentials.setTitle(longTitle);
        });
    }

    @Test
    public void testSanitizedTitle() {
        String xssInput = "<script>alert('Sanitization Test');</script>Developer";
        String expected = "Developer";

        testCredentials.setTitle(xssInput);

        assertEquals(expected, testCredentials.getTitle());
    }

    

    @Test
    public void testValidDepartment() {
        String[] departments = {"Sales", "Information Technology"};
        for (String department : departments) {
            testCredentials.setDepartment(department);
            assertEquals(department, testCredentials.getDepartment());
        }
    }

    @Test
    public void testLongDepartment() {
        assertThrows(IllegalArgumentException.class, () -> {
            String longDepartment = RandomStringUtils.insecure().next(65);
            testCredentials.setDepartment(longDepartment);
        });
    }

    @Test
    public void testSanitizedDepartment() {
        String xssInput = "<script>alert('Sanitization Test');</script>Information Technology";
        String expected = "Information Technology";

        testCredentials.setDepartment(xssInput);

        assertEquals(expected, testCredentials.getDepartment());
    }


    @Test
    public void testValidLocation() {
        String[] locations = {"United States", "Japan"};
        for (String location : locations) {
            testCredentials.setLocation(location);
            assertEquals(location, testCredentials.getLocation());
        }
    }

    @Test
    public void testNullLocation() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCredentials.setLocation(null);
        });
    }

    @Test
    public void testLongLocation() {
        assertThrows(IllegalArgumentException.class, () -> {
            String longLocation = RandomStringUtils.insecure().next(65);
            testCredentials.setLocation(longLocation);
        });
    }

    @Test
    public void testSanitizedLocation() {
        String xssInput = "<script>alert('Sanitization Test');</script>United States";
        String expected = "United States";

        testCredentials.setLocation(xssInput);

        assertEquals(expected, testCredentials.getLocation());
    }


}
