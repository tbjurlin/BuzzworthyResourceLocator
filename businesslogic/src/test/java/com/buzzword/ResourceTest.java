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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        String longTitle = RandomStringUtils.randomAlphanumeric(65);
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

}

