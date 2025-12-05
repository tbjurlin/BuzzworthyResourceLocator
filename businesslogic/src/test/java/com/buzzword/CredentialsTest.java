package com.buzzword;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void testEmptyTitle() {
        testCredentials.setTitle("");
        assertEquals("", testCredentials.getTitle());
    }

    @Test
    public void testSanitizedTitle() {
        String xssInput = "<script>alert('Sanitization Test');</script>Developer";
        String expected = "Developer";

        testCredentials.setTitle(xssInput);

        assertEquals(expected, testCredentials.getTitle());
    }

    @Test
    public void testNullTitleThrows() {
        // Null input is not allowed by the sanitizer
        assertThrows(IllegalArgumentException.class, () -> {
            testCredentials.setTitle(null);
        });
    }

    @Test
    public void testSetFirstNameValid() {
        testCredentials.setFirstName("John");
        assertEquals("John", testCredentials.getFirstName());
    }

    @Test
    public void testSetFirstNameNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCredentials.setFirstName(null);
        });
    }

    @Test
    public void testSetLastNameValid() {
        testCredentials.setLastName("Doe");
        assertEquals("Doe", testCredentials.getLastName());
    }

    @Test
    public void testSetLastNameNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            testCredentials.setLastName(null);
        });
    }

    @Test
    public void testGetLastNameDefault() {
        // Should return empty string by default since Name is now initialized
        assertNotNull(testCredentials.getLastName());
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
    public void testEmptyDepartment() {
        testCredentials.setDepartment("");
        assertEquals("", testCredentials.getDepartment());
    }

    @Test
    public void testSanitizedDepartment() {
        String xssInput = "<script>alert('Sanitization Test');</script>Information Technology";
        String expected = "Information Technology";

        testCredentials.setDepartment(xssInput);

        assertEquals(expected, testCredentials.getDepartment());
    }

    @Test
    public void testNullDepartmentThrows() {
        // Null input is not allowed by the sanitizer
        assertThrows(IllegalArgumentException.class, () -> {
            testCredentials.setDepartment(null);
        });
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
    public void testEmptyLocation() {
        testCredentials.setLocation("");
        assertEquals("", testCredentials.getLocation());
    }

    @Test
    public void testSanitizedLocation() {
        String xssInput = "<script>alert('Sanitization Test');</script>United States";
        String expected = "United States";

        testCredentials.setLocation(xssInput);

        assertEquals(expected, testCredentials.getLocation());
    }


}
