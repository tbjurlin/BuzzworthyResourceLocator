package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReviewFlagTest {

    private ReviewFlag testReviewFlag;

    @BeforeEach
    public void setUp() {
        testReviewFlag = new ReviewFlag();
    }

    @Test
    public void testReviewFlagConstruction() {
        assertNotNull(testReviewFlag, "ReviewFlag should be constructed successfully");
    }

    @Test
    public void testReviewFlagInheritsFromComment() {
        // Test that ReviewFlag inherits Comment functionality
        testReviewFlag.setContents("This resource needs review");
        assertEquals("This resource needs review", testReviewFlag.getContents());
    }

    @Test
    public void testReviewFlagInheritsRecordFields() {
        // Test that ReviewFlag inherits Record functionality through Comment
        testReviewFlag.setCreatorId(123);
        assertEquals(123, testReviewFlag.getCreatorId());

        testReviewFlag.setId(456);
        assertEquals(456, testReviewFlag.getId());

        testReviewFlag.setFirstName("John");
        assertEquals("John", testReviewFlag.getFirstName());

        testReviewFlag.setLastName("Doe");
        assertEquals("Doe", testReviewFlag.getLastName());
    }

    @Test
    public void testSetContentsNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testReviewFlag.setContents(null));
    }

    @Test
    public void testSetContentsEmptyAfterSanitizeThrows() {
        String bad = "<script>alert()</script>";
        assertThrows(IllegalArgumentException.class, () -> testReviewFlag.setContents(bad));
    }

    @Test
    public void testSetContentsValid() {
        testReviewFlag.setContents("Inappropriate content flagged");
        assertEquals("Inappropriate content flagged", testReviewFlag.getContents());
    }

    @Test
    public void testSetContentsTrimsAndSanitizes() {
        String input = "   Flagged <b>content</b>   ";
        testReviewFlag.setContents(input);
        assertEquals("Flagged content", testReviewFlag.getContents());
    }

    @Test
    public void testSetIsEditedValid() {
        testReviewFlag.setIsEdited(true);
        assertEquals(true, testReviewFlag.getIsEdited());
    }

    @Test
    public void testSetCurrentUserCanDeleteValid() {
        testReviewFlag.setCurrentUserCanDelete(true);
        assertEquals(true, testReviewFlag.getCurrentUserCanDelete());
    }

    @Test
    public void testSetCurrentUserCanEditValid() {
        testReviewFlag.setCurrentUserCanEdit(true);
        assertEquals(true, testReviewFlag.getCurrentUserCanEdit());
    }
}
