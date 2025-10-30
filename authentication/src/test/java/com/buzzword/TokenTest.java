package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void tokenRejectsMissingSignature() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJsYXN0X25hbWUiOiJTYXZhZ2UiLCJsb2NhdGlvbiI6IlVuaXRlZCBTdGF0ZXMiLCJpZCI6MTQsImRlcGFydG1lbnQiOiJTYWxlcyIsInRpdGxlIjoiU2FsZXMgQWdlbnQiLCJmaXJzdF9uYW1lIjoiRmFyciIsInN1YiI6IkZhcnIgU2F2YWdlIiwiaWF0IjoxNzYxODMzODk0LCJleHAiOjE3NjE4Mzc0OTR9.";
        assertThrows(IllegalArgumentException.class, () -> {
            token.setToken(invalidToken);
        });
        assertNotEquals(invalidToken, token.getToken());
    }

    @Test
    void tokenRejectsMissingPayload() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9..P-DuzzrIDzkq_jxYdLPhLYQ0nHGw6Db4KDsI0scMfGA";
        assertThrows(IllegalArgumentException.class, () -> {
            token.setToken(invalidToken);
        });
        assertNotEquals(invalidToken, token.getToken());
    }

    @Test
    void tokenRejectsMissingHeader() {
        String invalidToken = ".eyJsYXN0X25hbWUiOiJTYXZhZ2UiLCJsb2NhdGlvbiI6IlVuaXRlZCBTdGF0ZXMiLCJpZCI6MTQsImRlcGFydG1lbnQiOiJTYWxlcyIsInRpdGxlIjoiU2FsZXMgQWdlbnQiLCJmaXJzdF9uYW1lIjoiRmFyciIsInN1YiI6IkZhcnIgU2F2YWdlIiwiaWF0IjoxNzYxODMzODk0LCJleHAiOjE3NjE4Mzc0OTR9.P-DuzzrIDzkq_jxYdLPhLYQ0nHGw6Db4KDsI0scMfGA";
        assertThrows(IllegalArgumentException.class, () -> {
            token.setToken(invalidToken);
        });
        assertNotEquals(invalidToken, token.getToken());
    }

    @Test
    void tokenRequiresFirstPeriod() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9eyJsYXN0X25hbWUiOiJTYXZhZ2UiLCJsb2NhdGlvbiI6IlVuaXRlZCBTdGF0ZXMiLCJpZCI6MTQsImRlcGFydG1lbnQiOiJTYWxlcyIsInRpdGxlIjoiU2FsZXMgQWdlbnQiLCJmaXJzdF9uYW1lIjoiRmFyciIsInN1YiI6IkZhcnIgU2F2YWdlIiwiaWF0IjoxNzYxODMzODk0LCJleHAiOjE3NjE4Mzc0OTR9.P-DuzzrIDzkq_jxYdLPhLYQ0nHGw6Db4KDsI0scMfGA";
        assertThrows(IllegalArgumentException.class, () -> {
            token.setToken(invalidToken);
        });
        assertNotEquals(invalidToken, token.getToken());
    }

    @Test
    void tokenRequiresSecondPeriod() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJsYXN0X25hbWUiOiJTYXZhZ2UiLCJsb2NhdGlvbiI6IlVuaXRlZCBTdGF0ZXMiLCJpZCI6MTQsImRlcGFydG1lbnQiOiJTYWxlcyIsInRpdGxlIjoiU2FsZXMgQWdlbnQiLCJmaXJzdF9uYW1lIjoiRmFyciIsInN1YiI6IkZhcnIgU2F2YWdlIiwiaWF0IjoxNzYxODMzODk0LCJleHAiOjE3NjE4Mzc0OTR9P-DuzzrIDzkq_jxYdLPhLYQ0nHGw6Db4KDsI0scMfGA";
        assertThrows(IllegalArgumentException.class, () -> {
            token.setToken(invalidToken);
        });
        assertNotEquals(invalidToken, token.getToken());
    }

    @Test
    void tokenRejectsMoreThanTwoPeriods() {
        String invalidToken = "e.yJhbGciOiJIUzI1NiJ9.eyJsYXN0X25hbWUiOiJTYXZhZ2UiLCJsb2NhdGlvbiI6IlVuaXRlZCBTdGF0ZXMiLCJpZCI6MTQsImRlcGFydG1lbnQiOiJTYWxlcyIsInRpdGxlIjoiU2FsZXMgQWdlbnQiLCJmaXJzdF9uYW1lIjoiRmFyciIsInN1YiI6IkZhcnIgU2F2YWdlIiwiaWF0IjoxNzYxODMzODk0LCJleHAiOjE3NjE4Mzc0OTR9.P-DuzzrIDzkq_jxYdLPhLYQ0nHGw6Db4KDsI0scMfGA";
        assertThrows(IllegalArgumentException.class, () -> {
            token.setToken(invalidToken);
        });
        assertNotEquals(invalidToken, token.getToken());
    }

    @Test
    void tokenRejectsTooShort() {
        // TODO: Need length validation.
    }

    @Test
    void tokenRejectsTooLong() {
        // TODO: Need length validation.
    }
}
