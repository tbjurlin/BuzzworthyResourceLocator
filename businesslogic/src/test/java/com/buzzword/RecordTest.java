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
        assertEquals(nowDate, testRecord.getCreationDate());
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
