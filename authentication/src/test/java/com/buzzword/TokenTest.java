package com.buzzword;

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
