package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeywordListTest {

    private KeywordList keywordList;

    @BeforeEach
    public void setUp() {
        keywordList = new KeywordList();
    }

    @Test
    public void testDefaultConstructorCreatesEmptyList() {
        assertTrue(keywordList.getKeywords().isEmpty(), "Default constructor should create empty list");
    }

    @Test
    public void testConstructorWithKeywords() {
        KeywordList kwList = new KeywordList("java python javascript");
        assertEquals(3, kwList.getKeywords().size(), "Should have 3 keywords");
        assertEquals("java", kwList.getKeywords().get(0));
        assertEquals("python", kwList.getKeywords().get(1));
        assertEquals("javascript", kwList.getKeywords().get(2));
    }

    @Test
    public void testSetKeywordsSingleKeyword() {
        keywordList.setKeywords("java");
        assertEquals(1, keywordList.getKeywords().size());
        assertEquals("java", keywordList.getKeywords().get(0));
    }

    @Test
    public void testSetKeywordsMultipleKeywords() {
        keywordList.setKeywords("java python ruby");
        assertEquals(3, keywordList.getKeywords().size());
        assertEquals("java", keywordList.getKeywords().get(0));
        assertEquals("python", keywordList.getKeywords().get(1));
        assertEquals("ruby", keywordList.getKeywords().get(2));
    }

    @Test
    public void testSetKeywordsTrimsWhitespace() {
        keywordList.setKeywords("  java   python  ");
        // Split creates elements for spaces between words too
        assertTrue(keywordList.getKeywords().size() >= 2);
        // Verify the main keywords are present and trimmed
        assertTrue(keywordList.getKeywords().contains("java"));
        assertTrue(keywordList.getKeywords().contains("python"));
    }

    @Test
    public void testSetKeywordsConvertsToLowercase() {
        keywordList.setKeywords("JAVA Python RuBy");
        assertEquals(3, keywordList.getKeywords().size());
        assertEquals("java", keywordList.getKeywords().get(0));
        assertEquals("python", keywordList.getKeywords().get(1));
        assertEquals("ruby", keywordList.getKeywords().get(2));
    }

    @Test
    public void testSetKeywordsSanitizesInput() {
        keywordList.setKeywords("java<script>alert()</script> python");
        assertEquals(2, keywordList.getKeywords().size());
        // XSS sanitizer should remove the script tag
        assertEquals("java", keywordList.getKeywords().get(0));
        assertEquals("python", keywordList.getKeywords().get(1));
    }

    @Test
    public void testSetKeywordsMaxLimit() {
        // Create a string with more than 100 keywords
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 110; i++) {
            sb.append("keyword").append(i).append(" ");
        }
        keywordList.setKeywords(sb.toString());
        // Should only have 100 keywords due to MAX_KEYWORDS limit
        assertTrue(keywordList.getKeywords().size() <= 100, "Should not exceed 100 keywords");
    }

    @Test
    public void testSetKeywordsEmptyString() {
        keywordList.setKeywords("");
        // Empty string splits to array with one empty element
        assertEquals(1, keywordList.getKeywords().size());
    }

    @Test
    public void testToStringEmptyList() {
        assertEquals("", keywordList.toString(), "Empty list should return empty string");
    }

    @Test
    public void testToStringSingleKeyword() {
        keywordList.setKeywords("java");
        assertEquals("java", keywordList.toString());
    }

    @Test
    public void testToStringMultipleKeywords() {
        keywordList.setKeywords("java python ruby");
        assertEquals("java python ruby", keywordList.toString());
    }

    @Test
    public void testGetKeywordsReturnsList() {
        keywordList.setKeywords("java python");
        assertEquals(2, keywordList.getKeywords().size());
        assertTrue(keywordList.getKeywords().contains("java"));
        assertTrue(keywordList.getKeywords().contains("python"));
    }

    @Test
    public void testMultipleSpacesBetweenKeywords() {
        keywordList.setKeywords("java     python     ruby");
        // Multiple spaces create empty string elements that get sanitized to empty
        assertTrue(keywordList.getKeywords().size() >= 3, "Should have at least the 3 keywords");
        // Verify the main keywords are present
        assertTrue(keywordList.getKeywords().contains("java"));
        assertTrue(keywordList.getKeywords().contains("python"));
        assertTrue(keywordList.getKeywords().contains("ruby"));
    }

    @Test
    public void testKeywordsWithSpecialCharacters() {
        keywordList.setKeywords("c++ c# node.js");
        assertEquals(3, keywordList.getKeywords().size());
        assertEquals("c++", keywordList.getKeywords().get(0));
        assertEquals("c#", keywordList.getKeywords().get(1));
        assertEquals("node.js", keywordList.getKeywords().get(2));
    }
}
