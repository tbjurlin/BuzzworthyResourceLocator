package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommentsTest {

    private Comment testComment;

    @BeforeEach
    public void setUp() {
        testComment = new Comment();
    }

    @Test
    public void testSetContentsNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testComment.setContents(null));
    }

    @Test
    public void testSetContentsEmptyAfterSanitizeThrows() {
        // input that sanitizes to empty (only script tag)
        String bad = "<script>alert()</script>";
        assertThrows(IllegalArgumentException.class, () -> testComment.setContents(bad));
    }

    @Test
    public void testSetContentsWhitespaceOnlyThrows() {
        String onlySpaces = "    ";
        assertThrows(IllegalArgumentException.class, () -> testComment.setContents(onlySpaces));
    }

    @Test
    public void testSetContentsTooLongThrows() {
        StringBuilder sb = new StringBuilder();
        // create 201 characters to exceed the 200 char limit
        for (int i = 0; i < 201; i++) sb.append('a');
        String longContents = sb.toString();
        assertThrows(IllegalArgumentException.class, () -> testComment.setContents(longContents));
    }

    @Test
    public void testSetContentsValidMinAndMax() {
        // min length 1
        testComment.setContents("x");
        assertEquals("x", testComment.getContents());

        // max length 200 should be allowed
    StringBuilder sb = new StringBuilder();
    // create exactly 200 characters to test upper-bound acceptance
    for (int i = 0; i < 200; i++) sb.append('b');
        String maxContents = sb.toString();
        testComment.setContents(maxContents);
        assertEquals(maxContents, testComment.getContents());
    }

    @Test
    public void testSetContentsTrimsAndSanitizes() {
        String input = "   Hello <b>world</b>   ";
        // With default rules (Safelist.none()) tags are removed and result trimmed
        testComment.setContents(input);
        assertEquals("Hello world", testComment.getContents());
    }

}
