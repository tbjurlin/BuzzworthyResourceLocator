package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NameTest {
    private DummyName testName = null;

    @BeforeEach
    void setup() {
        testName = new DummyName();
    }

    @Test
    public void testValidCreatorFirstName() {
        String[] names = {"Bob", "aLice", "CARL", "dave", "li'l ernie"};
        for (String name : names) {
            testName.setFirstName(name);
        }
    }

    @Test
    public void testNullCreatorFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testName.setFirstName(null);
        });
    }

    @Test
    public void testEmptyCreatorFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testName.setFirstName("");
        });
    }

    @Test
    public void testLongCreatorFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            String longName = RandomStringUtils.insecure().next(65);
            testName.setFirstName(longName);
        });
    }

    @Test
    public void testSanitizedFirstName() {
        String xssInput = "<script>alert('Sanitization Test');</script>Bob";
        String expected = "Bob";

        testName.setFirstName(xssInput);

        assertEquals(expected, testName.getFirstName());
    }

    @Test
    public void testValidCreatorLastName() {
        String[] names = {"Smith", "O'Brien", "Forsythe-Marsdon", "johnson", "PO", "last name"};
        for (String name : names) {
            testName.setLastName(name);
        }
    }

    @Test
    public void testNullCreatorLastName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testName.setLastName(null);
        });
    }

    @Test
    public void testEmptyCreatorLastName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testName.setLastName("");
        });
    }

    @Test
    public void testLongCreatorLastName() {
        assertThrows(IllegalArgumentException.class, () -> {
            String longName = RandomStringUtils.insecure().next(65);
            testName.setLastName(longName);
        });
    }

    @Test
    public void testSanitizedLastName() {
        String xssInput = "<script>alert('Sanitization Test');</script>Smith";
        String expected = "Smith";

        testName.setLastName(xssInput);

        assertEquals(expected, testName.getLastName());
    }
}
