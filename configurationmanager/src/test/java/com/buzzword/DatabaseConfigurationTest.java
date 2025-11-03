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

    @Mock
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
        assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseConfigurationImpl(manager);
        });
    }
}
