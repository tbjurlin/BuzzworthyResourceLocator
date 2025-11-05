package com.buzzword;

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
