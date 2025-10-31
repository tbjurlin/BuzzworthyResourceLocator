package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.lang3.RandomStringUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Unit test .
 */
public class ResourceTest {

    private Resource testResource = null;

    @BeforeEach
    void setup() {
        testResource = new Resource();
    }

    @Test
    public void testInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            testResource.setId(-1);
        }); 
    }

    @Test
    public void testValidId() {
        testResource.setId(1);
        assertEquals(1, testResource.getId()); 
    }

    @Test
    public void testInvalidCreatorId() {
        assertThrows(IllegalArgumentException.class, () -> {
            testResource.setCreatorId(-1);
        });
    }

    @Test
    public void testValidCreatorId() {
        testResource.setCreatorId(1);
        assertEquals(1, testResource.getCreatorId()); 
    }

    @Test
    public void testNullCreatorFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testResource.setCreatorFirstName(null);
        });
    }

    @Test
    public void testEmptyCreatorFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testResource.setCreatorFirstName("");
        });
    }

    @Test
    public void testLongCreatorFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            String longName = RandomStringUtils.random(41);
            testResource.setCreatorFirstName(longName);
        });
    }

    @Test
    public void testValidCreatorFirstName() {

    }

}

