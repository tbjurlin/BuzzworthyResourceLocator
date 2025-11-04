package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.lang3.RandomStringUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import java.time.Instant;
import java.time.Clock;
import java.time.ZoneId;
import java.time.Duration;

import java.util.Date;


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
    public void testValidId() {
        testRecord.setId(1);
        assertEquals(1, testRecord.getId()); 
    }

    @Test
    public void testValidZeroId() {
        testRecord.setId(0);
        assertEquals(0, testRecord.getId()); 
    }

    @Test
    public void testInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            testRecord.setId(-1);
        }); 
    }

    @Test
    public void testValidCreatorId() {
        testRecord.setCreatorId(1);
        assertEquals(1, testRecord.getCreatorId()); 
    }

    @Test
    public void testInvalidCreatorId() {
        assertThrows(IllegalArgumentException.class, () -> {
            testRecord.setCreatorId(-1);
        });
    }

    @Test
    public void testValidDate() {
        Date nowDate = new Date();
        testRecord.setCreationDate(nowDate);
    }

    @Test
    public void testNullCreationDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            testRecord.setCreationDate(null);
        });
    }

    @Test
    public void testFutureDate() {
        Clock nowClock = Clock.systemUTC();
        Duration clockOffset = Duration.ofDays(1);
        Clock futureClock = Clock.offset(nowClock, clockOffset);
        Instant instant = Instant.now(futureClock);
        Date futureDate = Date.from(instant);

        // this.creationDate =  Date.from(instant);
        assertThrows(IllegalArgumentException.class, () -> {
            testRecord.setCreationDate(futureDate);
        });
    }

}
