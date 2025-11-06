package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseConfigurationTest {

    @Mock(strictness = Mock.Strictness.LENIENT)
    ConfigurationManagerImpl manager;

    @BeforeEach
    void setUpMock() {
        when(manager.getDatabaseName()).thenReturn("databaseName");
        when(manager.getDatabaseUserName()).thenReturn("userName");
        when(manager.getDatabasePassword()).thenReturn("password");
        when(manager.getDatabaseHost()).thenReturn("hostname");
        when(manager.getDatabasePort()).thenReturn("8080");
        when(manager.getDatabaseMinPoolSize()).thenReturn("5");
        when(manager.getDatabaseMaxPoolSize()).thenReturn("10");
    }

    @Test
    void constructsValidDatabaseConfiguration() {

        DatabaseConfiguration dbConfig = new DatabaseConfigurationImpl(manager);

        assertEquals("databaseName", dbConfig.getDatabaseName(), "Invalid database name.");
        assertEquals(5, dbConfig.getMinDatabaseConnections(), "Incorrect min pool connections.");
        assertEquals(10, dbConfig.getMaxDatabaseConnections(), "Incorrect max pool connections.");
        String connectionString = "mongodb://userName:password@hostname:8080/?authSource=admin";
        assertEquals(connectionString, dbConfig.getDatabaseConnectionString(), "Invalid connection string.");
    }

    @Test
    void rejectsNullDatabaseName() {
        when(manager.getDatabaseName()).thenReturn(null);
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsNullDatabaseUserName() {
        when(manager.getDatabaseUserName()).thenReturn(null);
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsNullDatabasePassword() {
        when(manager.getDatabasePassword()).thenReturn(null);
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsNullDatabaseHost() {
        when(manager.getDatabaseHost()).thenReturn(null);
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsNullDatabasePort() {
        when(manager.getDatabasePort()).thenReturn(null);
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsNullDatabaseMinPoolSize() {
        when(manager.getDatabaseMinPoolSize()).thenReturn(null);
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsNullDatabaseMaxPoolSize() {
        when(manager.getDatabaseMaxPoolSize()).thenReturn(null);
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void passwordIsURLEncoded() {
        when(manager.getDatabasePassword()).thenReturn("$:/?#[]@");
        DatabaseConfiguration dbConfig = new DatabaseConfigurationImpl(manager);
        String connectionString = "mongodb://userName:%24%3A%2F%3F%23%5B%5D%40@hostname:8080/?authSource=admin";
        assertEquals(connectionString, dbConfig.getDatabaseConnectionString());
    }

    @Test
    void rejectsEmptyPassword() {
        when(manager.getDatabasePassword()).thenReturn("");
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsEmptyHost() {
        when(manager.getDatabaseHost()).thenReturn("");
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsTooSmallPort() {
        when(manager.getDatabasePort()).thenReturn("0");
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsTooLargePort() {
        when(manager.getDatabasePort()).thenReturn("65536");
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsTooSmallMinPool() {
        when(manager.getDatabaseMinPoolSize()).thenReturn("-1");
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsTooNonNumericMinPool() {
        when(manager.getDatabaseMinPoolSize()).thenReturn("five");
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsTooSmallMaxPool() {
        when(manager.getDatabaseMaxPoolSize()).thenReturn("0");
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsTooNonNumericMaxPool() {
        when(manager.getDatabaseMaxPoolSize()).thenReturn("five");
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }

    @Test
    void rejectsMaxPoolLessThanMinPool() {
        when(manager.getDatabaseMinPoolSize()).thenReturn("10");
        when(manager.getDatabaseMaxPoolSize()).thenReturn("5");
        assertThrows(ConfigurationException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }
}
