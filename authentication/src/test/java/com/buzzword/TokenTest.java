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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class TokenTest {
    private final Token token = new Token();

    @Test
    void tokenAcceptsValidInput() {
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJsYXN0X25hbWUiOiJTYXZhZ2UiLCJsb2NhdGlvbiI6IlVuaXRlZCBTdGF0ZXMiLCJpZCI6MTQsImRlcGFydG1lbnQiOiJTYWxlcyIsInRpdGxlIjoiU2FsZXMgQWdlbnQiLCJmaXJzdF9uYW1lIjoiRmFyciIsInN1YiI6IkZhcnIgU2F2YWdlIiwiaWF0IjoxNzYxODMzODk0LCJleHAiOjE3NjE4Mzc0OTR9.P-DuzzrIDzkq_jxYdLPhLYQ0nHGw6Db4KDsI0scMfGA";
        token.setToken(validToken);
        assertEquals(validToken, token.getToken());
    }

    @Test
    void tokenRejectsTooShort() {
        char[] array = new char[249];
        Arrays.fill(array, 'a');
        String tooShortToken = new String(array);
        assertThrows(IllegalArgumentException.class, () -> {
            token.setToken(tooShortToken);
        });
        assertNotEquals(tooShortToken, token.getToken());
    }

    @Test
    void tokenRejectsTooLong() {
        char[] array = new char[501];
        Arrays.fill(array, 'a');
        String tooLongToken = new String(array);
        assertThrows(IllegalArgumentException.class, () -> {
            token.setToken(tooLongToken);
        });
        assertNotEquals(tooLongToken, token.getToken());
    }

    @Test
    void tokenRejectsNull() {
        String nullToken = null;
        assertThrows(IllegalArgumentException.class, () -> {
            token.setToken(nullToken);
        });
    }
}
