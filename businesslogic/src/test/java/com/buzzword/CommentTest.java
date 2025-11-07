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
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommentTest {

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
        for (int i = 1; i <= 201; i++) sb.append('a');
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
