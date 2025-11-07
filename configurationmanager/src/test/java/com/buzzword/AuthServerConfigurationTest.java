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
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServerConfigurationTest {
    
    @Mock
    ConfigurationManagerImpl manager;

    @Test
    void throwsErrorOnMissingURLInput() {
        when(manager.getAuthServerHost()).thenReturn(null);
        when(manager.getAuthServerPort()).thenReturn("8080");
        when(manager.getAuthServerSubdomain()).thenReturn("/auth");

        assertThrows(ConfigurationException.class, () -> {
            new AuthServerConfigurationImpl(manager);
        });

    }

    @Test
    void throwsErrorOnMissingSubdomainInput() {
        when(manager.getAuthServerHost()).thenReturn("https://example.com");
        when(manager.getAuthServerPort()).thenReturn("8080");
        when(manager.getAuthServerSubdomain()).thenReturn(null);

        assertThrows(ConfigurationException.class, () -> {
            new AuthServerConfigurationImpl(manager);
        });

    }

    @Test
    void throwsErrorOnEmptyURLInput() {
        when(manager.getAuthServerHost()).thenReturn("");
        when(manager.getAuthServerPort()).thenReturn("8080");
        when(manager.getAuthServerSubdomain()).thenReturn("/auth");

        assertThrows(ConfigurationException.class, () -> {
            new AuthServerConfigurationImpl(manager);
        });

    }

    @Test
    void throwsErrorOnEmptySubdomainInput() {
        when(manager.getAuthServerHost()).thenReturn("https://example.com");
        when(manager.getAuthServerPort()).thenReturn("8080");
        when(manager.getAuthServerSubdomain()).thenReturn("");

        assertThrows(ConfigurationException.class, () -> {
            new AuthServerConfigurationImpl(manager);
        });

    }

    @Test
    void throwsErrorOnInvalidPortInput() {
        when(manager.getAuthServerHost()).thenReturn("https://example.com");
        when(manager.getAuthServerPort()).thenReturn("Not a port number");
        when(manager.getAuthServerSubdomain()).thenReturn("/auth");

        assertThrows(ConfigurationException.class, () -> {
            new AuthServerConfigurationImpl(manager);
        });

    }

    @Test
    void constructsURLWithPort() {
        when(manager.getAuthServerHost()).thenReturn("https://example.com");
        when(manager.getAuthServerPort()).thenReturn("8080");
        when(manager.getAuthServerSubdomain()).thenReturn("/auth");

        AuthServerConfiguration config = new AuthServerConfigurationImpl(manager);

        assertEquals("https://example.com:8080/auth", config.getAuthServerConnectionString());
    }

    @Test
    void constructsURLWithoutPort() {
        when(manager.getAuthServerHost()).thenReturn("https://example.com");
        when(manager.getAuthServerPort()).thenReturn(null);
        when(manager.getAuthServerSubdomain()).thenReturn("/auth");

        AuthServerConfiguration config = new AuthServerConfigurationImpl(manager);

        assertEquals("https://example.com/auth", config.getAuthServerConnectionString());
    }
}
