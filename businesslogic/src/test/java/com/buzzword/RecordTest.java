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
public class RecordTest {

    private DummyRecord testRecord = null;

    @BeforeEach
    void setup() {
        testRecord = new DummyRecord();
    }

    @Test
    public void testInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            testRecord.setId(-1);
        }); 
    }

    @Test
    public void testValidId() {
        testRecord.setId(1);
        assertEquals(1, testRecord.getId()); 
    }

    @Test
    public void testInvalidCreatorId() {
        assertThrows(IllegalArgumentException.class, () -> {
            testRecord.setCreatorId(-1);
        });
    }

    @Test
    public void testValidCreatorId() {
        testRecord.setCreatorId(1);
        assertEquals(1, testRecord.getCreatorId()); 
    }

    @Test
    public void testNullCreatorFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testRecord.setCreatorFirstName(null);
        });
    }

    @Test
    public void testEmptyCreatorFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testRecord.setCreatorFirstName("");
        });
    }

    @Test
    public void testLongCreatorFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            String longName = RandomStringUtils.insecure().next(41);
            testRecord.setCreatorFirstName(longName);
        });
    }

    @Test
    public void testValidCreatorFirstName() {

    }

}
