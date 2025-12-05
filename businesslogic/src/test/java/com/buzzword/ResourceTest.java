package com.buzzword;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests
 */
public class ResourceTest {

    private Resource testResource = null;

    @BeforeEach
    void setup() {
        testResource = new Resource();
    }

    @Test
    public void testSetTitleNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testResource.setTitle(null));
    }

    @Test
    public void testSetTitleEmptyAfterSanitizeThrows() {
        // input that sanitizes to empty (only script tag)
        String bad = "<script>alert()</script>";
        assertThrows(IllegalArgumentException.class, () -> testResource.setTitle(bad));
    }

    @Test
    public void testSetTitleTooLongThrows() {
        String longTitle = RandomStringUtils.insecure().nextAlphanumeric(65);
        assertThrows(IllegalArgumentException.class, () -> testResource.setTitle(longTitle));
    }

    @Test
    public void testSetTitleValid() {
        String input = "   Your Mother   ";
        testResource.setTitle(input);
        // sanitizer trims whitespace
        assertEquals("Your Mother", testResource.getTitle());
    }

    @Test
    public void testSetUrlNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testResource.setUrl(null));
    }

    @Test
    public void testSetUrlEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> testResource.setUrl("   "));
    }

    @Test
    public void testSetUrlInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () -> testResource.setUrl("not-a-url"));
    }

    @Test
    public void testSetUrlValidHttpAndHttps() {
        testResource.setUrl(" http://example.com/path ");
        assertEquals("http://example.com/path", testResource.getUrl());

        testResource.setUrl("https://example.com/");
        assertEquals("https://example.com/", testResource.getUrl());
    }

    @Test
    public void testSetDescriptionNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testResource.setDescription(null));
    }

    @Test
    public void testSetDescriptionEmptyAfterSanitizeThrows() {
        String bad = "<script>bad</script>";
        assertThrows(IllegalArgumentException.class, () -> testResource.setDescription(bad));
    }

    @Test
    public void testSetDescriptionValid() {
        String input = "   This is a description.   ";
        testResource.setDescription(input);
        assertEquals("This is a description.", testResource.getDescription());
    }

    @Test
    public void testSetCommentsNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testResource.setComments(null));
    }

    @Test
    public void testSetCommentsValid() {
        List<Comment> list = new ArrayList<>();
        testResource.setComments(list);
        assertEquals(list, testResource.getComments());
    }

    @Test
    public void testSetReviewFlagsAndUpvotesNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testResource.setReviewFlags(null));
        assertThrows(IllegalArgumentException.class, () -> testResource.setUpvotes(null));
    }

    @Test
    public void testSetReviewFlagsAndUpvotesValid() {
        List<ReviewFlag> rf = new ArrayList<>();
        List<Upvote> uv = new ArrayList<>();
        testResource.setReviewFlags(rf);
        testResource.setUpvotes(uv);
        assertEquals(rf, testResource.getReviewFlags());
        assertEquals(uv, testResource.getUpvotes());
    }

    @Test
    public void testSetUpvoteCount() {
        testResource.setUpvoteCount(5);
        assertEquals(5, testResource.getUpvoteCount());
        
        testResource.setUpvoteCount(0);
        assertEquals(0, testResource.getUpvoteCount());
    }

    @Test
    public void testIncrementUpvoteCount() {
        testResource.setUpvoteCount(0);
        testResource.incrementUpvoteCount();
        assertEquals(1, testResource.getUpvoteCount());
        
        testResource.incrementUpvoteCount();
        assertEquals(2, testResource.getUpvoteCount());
    }

    @Test
    public void testSetUpvotedByCurrentUser() {
        testResource.setUpvotedByCurrentUser(true);
        assertEquals(true, testResource.getUpvotedByCurrentUser());
        
        testResource.setUpvotedByCurrentUser(false);
        assertEquals(false, testResource.getUpvotedByCurrentUser());
    }

    @Test
    public void testSetCurrentUserUpvoteId() {
        testResource.setCurrentUserUpvoteId(42);
        assertEquals(42, testResource.getCurrentUserUpvoteId());
        
        testResource.setCurrentUserUpvoteId(0);
        assertEquals(0, testResource.getCurrentUserUpvoteId());
    }

    @Test
    public void testSetTitleSanitizesHtml() {
        String input = "<b>Bold Title</b>";
        testResource.setTitle(input);
        // Verify HTML is sanitized (exact output depends on sanitizer config)
        assertEquals("Bold Title", testResource.getTitle());
    }

    @Test
    public void testSetDescriptionSanitizesHtml() {
        String input = "<p>Test <strong>description</strong></p>";
        testResource.setDescription(input);
        // Description allows more HTML, so verify it still works
        assertTrue(testResource.getDescription().contains("description"));
    }

    @Test
    public void testSetTitleMaxLength() {
        // Test with exactly 64 characters (should pass)
        String validLength = RandomStringUtils.insecure().nextAlphanumeric(64);
        testResource.setTitle(validLength);
        assertEquals(validLength, testResource.getTitle());
    }

    @Test
    public void testSetDescriptionMaxLength() {
        // Test with a long description (should pass up to reasonable limit)
        String longDesc = RandomStringUtils.insecure().nextAlphanumeric(500);
        testResource.setDescription(longDesc);
        assertEquals(longDesc, testResource.getDescription());
    }

    @Test
    public void testSetUrlWithFragment() {
        String urlWithFragment = "https://example.com/path#section";
        testResource.setUrl(urlWithFragment);
        assertEquals(urlWithFragment, testResource.getUrl());
    }

}


