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

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NameTest {
    private Name testName = null;

    @BeforeEach
    void setup() {
        testName = new Name();
    }

    @Test
    public void testValidName() {
        String[] names = {"Bob", "aLice", "CARL", "dave", "li'l ernie"};
        for (String name : names) {
            testName.setName(name);
            assertEquals(name, testName.getName());
        }
    }

    @Test
    public void testNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testName.setName(null);
        });
    }

    @Test
    public void testEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            testName.setName(" ");
        });
    }

    @Test
    public void testLongName() {
        assertThrows(IllegalArgumentException.class, () -> {
            String longName = RandomStringUtils.insecure().next(65);
            testName.setName(longName);
        });
    }

    @Test
    public void testSanitizedName() {
        String xssInput = "<script>alert('Sanitization Test');</script>Bob";
        String expected = "Bob";

        testName.setName(xssInput);

        assertEquals(expected, testName.getName());
    }

}
