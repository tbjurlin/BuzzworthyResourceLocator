package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NameTest {
    private Name testName = null;

    @BeforeEach
    void setup() {
        testName = new Name();
    }

    @Test
    public void testValidName() {
        String[] names = {"Bob", "aLice", "CARL", "dave", "li'l ernie"};
        for (String name : names) {
            testName.setName(name);
            assertEquals(name, testName.getName());
        }
    }

    @Test
    public void testNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testName.setName(null);
        });
    }

    @Test
    public void testEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testName.setName(" ");
        });
    }

    @Test
    public void testLongName() {
        assertThrows(IllegalArgumentException.class, () -> {
            String longName = RandomStringUtils.insecure().next(65);
            testName.setName(longName);
        });
    }

    @Test
    public void testSanitizedName() {
        String xssInput = "<script>alert('Sanitization Test');</script>Bob";
        String expected = "Bob";

        testName.setName(xssInput);

        assertEquals(expected, testName.getName());
    }

}
