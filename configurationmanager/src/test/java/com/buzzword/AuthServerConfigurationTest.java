package com.buzzword;

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

        assertThrows(ConfigurationException.class, () -> {
            new AuthServerConfigurationImpl(manager);
        });

    }

    @Test
    void throwsErrorOnInvalidPortInput() {
        when(manager.getAuthServerHost()).thenReturn("https://example.com");
        when(manager.getAuthServerPort()).thenReturn("Not a port number");

        assertThrows(ConfigurationException.class, () -> {
            new AuthServerConfigurationImpl(manager);
        });

    }

    @Test
    void constructsURLWithPort() {
        when(manager.getAuthServerHost()).thenReturn("https://example.com");
        when(manager.getAuthServerPort()).thenReturn("8080");

        AuthServerConfiguration config = new AuthServerConfigurationImpl(manager);

        assertEquals("https://example.com:8080", config.getAuthServerConnectionString());
    }

    @Test
    void constructsURLWithoutPort() {
        when(manager.getAuthServerHost()).thenReturn("https://example.com");
        when(manager.getAuthServerPort()).thenReturn(null);

        AuthServerConfiguration config = new AuthServerConfigurationImpl(manager);

        assertEquals("https://example.com", config.getAuthServerConnectionString());
    }
}
