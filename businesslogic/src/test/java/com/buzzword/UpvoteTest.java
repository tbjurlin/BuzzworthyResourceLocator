package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpvoteTest {

    private Upvote testUpvote;

    @BeforeEach
    public void setUp() {
        testUpvote = new Upvote();
    }

    @Test
    public void testUpvoteConstruction() {
        assertNotNull(testUpvote, "Upvote should be constructed successfully");
        assertNotNull(testUpvote.getCreationDate(), "Creation date should be initialized");
    }

    @Test
    public void testUpvoteInheritsFromRecord() {
        // Test that Upvote inherits Record functionality
        testUpvote.setId(123);
        assertEquals(123, testUpvote.getId());

        testUpvote.setCreatorId(456);
        assertEquals(456, testUpvote.getCreatorId());
    }

    @Test
    public void testSetIdValid() {
        testUpvote.setId(100);
        assertEquals(100, testUpvote.getId());
    }

    @Test
    public void testSetIdZero() {
        testUpvote.setId(0);
        assertEquals(0, testUpvote.getId());
    }

    @Test
    public void testSetIdNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> testUpvote.setId(-1));
    }

    @Test
    public void testSetCreatorIdValid() {
        testUpvote.setCreatorId(200);
        assertEquals(200, testUpvote.getCreatorId());
    }

    @Test
    public void testSetCreatorIdZero() {
        testUpvote.setCreatorId(0);
        assertEquals(0, testUpvote.getCreatorId());
    }

    @Test
    public void testSetCreatorIdNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> testUpvote.setCreatorId(-1));
    }

    @Test
    public void testSetFirstNameValid() {
        testUpvote.setFirstName("Jane");
        assertEquals("Jane", testUpvote.getFirstName());
    }

    @Test
    public void testSetFirstNameNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testUpvote.setFirstName(null));
    }

    @Test
    public void testSetLastNameValid() {
        testUpvote.setLastName("Smith");
        assertEquals("Smith", testUpvote.getLastName());
    }

    @Test
    public void testSetLastNameNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testUpvote.setLastName(null));
    }

    @Test
    public void testSetCreationDateValid() {
        Date now = new Date();
        testUpvote.setCreationDate(now);
        assertEquals(now, testUpvote.getCreationDate());
    }

    @Test
    public void testSetCreationDateNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testUpvote.setCreationDate(null));
    }

    @Test
    public void testSetCreationDateFutureThrows() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // tomorrow
        assertThrows(IllegalArgumentException.class, () -> testUpvote.setCreationDate(futureDate));
    }

    @Test
    public void testIsEditedFlag() {
        testUpvote.setIsEdited(true);
        assertTrue(testUpvote.getIsEdited());
        
        testUpvote.setIsEdited(false);
        assertFalse(testUpvote.getIsEdited());
    }

    @Test
    public void testCurrentUserCanDeleteFlag() {
        testUpvote.setCurrentUserCanDelete(true);
        assertTrue(testUpvote.getCurrentUserCanDelete());
        
        testUpvote.setCurrentUserCanDelete(false);
        assertFalse(testUpvote.getCurrentUserCanDelete());
    }

    @Test
    public void testCurrentUserCanEditFlag() {
        testUpvote.setCurrentUserCanEdit(true);
        assertTrue(testUpvote.getCurrentUserCanEdit());
        
        testUpvote.setCurrentUserCanEdit(false);
        assertFalse(testUpvote.getCurrentUserCanEdit());
    }

    @Test
    public void testDefaultCreationDateNotNull() {
        assertNotNull(testUpvote.getCreationDate(), "Default creation date should not be null");
    }

    @Test
    public void testDefaultIsEditedFalse() {
        assertFalse(testUpvote.getIsEdited(), "Default isEdited should be false");
    }

    @Test
    public void testDefaultCurrentUserCanDeleteFalse() {
        assertFalse(testUpvote.getCurrentUserCanDelete(), "Default currentUserCanDelete should be false");
    }
}
